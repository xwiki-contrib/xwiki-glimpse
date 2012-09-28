#!/usr/bin/env python
import os
import json

def get_crontab():
    cron = {}
    cronfile = open('/var/spool/cron/crontabs/root','r')
    cron['Cron'] = '\n'.join([line for line in cronfile.readlines() if not line.startswith('#')])

    return json.dumps(cron)

if __name__ == '__main__':
    print get_crontab()
