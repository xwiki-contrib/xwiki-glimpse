from ConfigParser import ConfigParser
from threading import Thread
import httplib
import json
import glimpse_agent.constants
import glimpse_agent.plugins
import logging
import socket
import time

_DEFAULT_CONFIGURATION = {
                          'server' : '127.0.0.1:8080',
                          'update_interval' : '5',
                          'plugins_dirs' : '%s:%s' % (glimpse_agent.constants.SYSTEM_WIDE_PLUGINS_DIR, glimpse_agent.constants.USER_PLUGINS_DIR)
                          }

class AgentThread(Thread):
    def __init__(self, configuration):
        Thread.__init__(self)
        self.server = configuration.get('agent', 'server')
        self.update_interval = int(configuration.get('agent', 'update_interval'))
        self.plugins_dirs = configuration.get('agent', 'plugins_dirs').split(':')
        self.done = False

    def run(self):
        i = 0
        while not self.done:
            # Count up to update interval, sleeping one second at each step.
            # This is done in order to make the thread responsive to external events.
            if i >= self.update_interval:
                try:
                    self.send_agent_data()
                except Exception as e:
                    logging.warn("Unable to send data to %s: %s" % (self.server, e))
                i = 0
            else:
                i += 1

            time.sleep(1)

    def send_agent_data(self):
        services = self.execute_plugins()

        if not services:            
            return

        hostname = socket.gethostname()

        agent_data = {
                      'ip' : socket.gethostbyname(hostname),
                      'name' : hostname,
                      'services' : services
                      }

        connection = httplib.HTTPConnection(self.server)
        connection.request('POST', '/xwiki/rest/glimpse/agents', json.dumps(agent_data))
        response = connection.getresponse()
        
        if (response.status / 100) != 2:
            logging.warn("Server %s responded with a %s status: %s" % (self.server, response.status, response.read()))

        connection.close()

    def execute_plugins(self):
        result = []

        plugins = glimpse_agent.plugins.build_plugins_list(self.plugins_dirs)
        for plugin in plugins:
            result.append(glimpse_agent.plugins.execute_plugin(plugin))

        return result


def start(configuration):
    agent = AgentThread(configuration)
    agent.start()

    while agent.isAlive():
        try:
            agent.join(1)
        except KeyboardInterrupt:
            agent.done = True

def main():
    """Entry point for the Glimpse agent"""

    # Initialize configuration with the default configuration and add an 'agent' section. 
    # This is needed in order to make calls to configuration.get('agent', ...) not to fail if no configuration files are loaded.
    configuration = ConfigParser(_DEFAULT_CONFIGURATION)
    configuration.add_section('agent')

    # Read configuration files if present
    configuration.read([glimpse_agent.constants.SYSTEM_WIDE_CONFIGURATION_FILE_PATH, glimpse_agent.constants.USER_CONFIGURATION_FILE_PATH])

    # Initialize the application
    start(configuration)

if __name__ == '__main__':
    main()
