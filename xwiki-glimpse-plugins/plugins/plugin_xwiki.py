#!/usr/bin/env python
import os
import urllib
import json

###################################################
# CONFIGURATION
###################################################
TOMCAT_SERVER_INFO_URL = 'http://tomcat:tomcat@localhost:8080/manager/serverinfo'
XWIKI_WEBINF_DIRECTORY = '/home/fm/Applications/xwiki-enterprise-jetty-hsqldb-3.2-milestone-2/webapps/xwiki/WEB-INF'
###################################################

def get_xwiki_info():
    result = {}

    f = urllib.urlopen(TOMCAT_SERVER_INFO_URL)
    lines = f.read().split('\n')
    for line in lines:
        components = line.split(':', 2)
        if len(components) == 2:
            key, value = components
            if key == 'Tomcat Version':
                result['Tomcat version'] = value.strip()
            elif key == 'JVM Version':
                result['JVM version'] = value.strip()
            elif key == 'JVM Vendor':
                result['JVM vendor'] = value.strip()
    f.close()

    if os.path.exists(XWIKI_WEBINF_DIRECTORY):
        f = open(os.path.join(XWIKI_WEBINF_DIRECTORY, 'version.properties'))
        lines = f.readlines()
        for line in lines:
            components = line.split('=', 2)
            if len(components) == 2:
                key, value = components
                if key == 'version':
                    result['Version'] = value.strip()

        f.close()

        f = open(os.path.join(XWIKI_WEBINF_DIRECTORY, 'xwiki.cfg'))
        lines = f.readlines()
        for line in lines:
            components = line.split('=', 2)
            if len(components) == 2:
                key, value = components
                if key == 'xwiki.virtual':
                    if value.strip() == '1':
                        result['Farm'] = 'true'
                    else:
                        result['Farm'] = 'false'

        f.close()

    return json.dumps(result)

if __name__ == '__main__':
    print get_xwiki_info()
