from subprocess import Popen, PIPE
import fnmatch
import json
import glimpse_agent.constants
import os

def build_plugins_list(plugins_dirs):
        result = []

        for plugins_dir in plugins_dirs:
            if os.path.isdir(plugins_dir):
                for file in os.listdir(plugins_dir):
                    plugin_full_path = os.path.join(plugins_dir, file)
                    if fnmatch.fnmatch(file, '%s*' % glimpse_agent.constants.DEFAULT_PLUGIN_PREFIX) and os.access(plugin_full_path, os.X_OK):
                        result.append(plugin_full_path)

        return result

def execute_plugin(plugin_path):
        plugin = Popen(plugin_path, stdout = PIPE)
        status = plugin.wait()
        data = plugin.stdout.read()

        result = {}
        filename = os.path.basename(plugin_path).replace('plugin_', '')
        result['name'] = os.path.splitext(filename)[0]
        result['status'] = status

        try:
            # Check if the output is in the JSON format and decode it
            jsonData = json.loads(data)

            result['properties'] = jsonData
        except ValueError:
            result['properties'] = {}
            result['properties']['error'] = 'Plugin output not in JSON format'
            result['properties']['output'] = data

        return result
