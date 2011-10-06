import os

SYSTEM_WIDE_CONFIGURATION_FILE_PATH = '/etc/glimpse/agent.cfg'

SYSTEM_WIDE_PLUGINS_DIR = '/etc/glimpse/plugins'

USER_CONFIGURATION_FILE_PATH = os.path.expanduser('~/.glimpse/agent.cfg')

USER_PLUGINS_DIR = os.path.expanduser('~/.glimpse/plugins')

DEFAULT_PLUGIN_PREFIX = 'plugin_'
