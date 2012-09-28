#!/usr/bin/env python
import os
import json
import statvfs

def get_host_info():
    info = {}
    info['OS name'] = os.uname()[0]
    info['OS version'] = os.uname()[2]
    info['OS arch'] = os.uname()[4]

    if os.path.exists('/etc/debian_version'):
    	distrib_file = open('/etc/debian_version','r')
	info['Version'] = "Debian " + distrib_file.read().rstrip()
    elif os.path.exists('/etc/redhat-version'):
	distrib_file = open('/etc/redhat-release','r')
	info['Version'] = "RedHat " + distrib_file.read().rstrip()
    else:
	info['Version'] = "Unknown"

    st = os.statvfs('/')
    gb = 1024 * 1024 * 1024
    info['Total disk space'] = "%sGB" % str((st[statvfs.F_BLOCKS] * st[statvfs.F_FRSIZE]) / gb)
    info['Free disk space'] = "%sGB" % str((st[statvfs.F_BFREE] * st[statvfs.F_FRSIZE]) / gb)


    return json.dumps(info)

if __name__ == '__main__':
    print get_host_info()
