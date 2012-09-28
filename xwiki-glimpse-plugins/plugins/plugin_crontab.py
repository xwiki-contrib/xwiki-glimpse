#!/usr/bin/env python
import os
import json

def get_crontab():
    cron = {}

    # every user that have a non-empty crontab
    for user in os.listdir('/var/spool/cron/crontabs/'):
        cronfile = open('/var/spool/cron/crontabs/%s' % (user),'r')
        # crontab lines are already separated by a \n character, no need to add one
	cron[user] = ''.join([line for line in cronfile.readlines() if not line.startswith('#')])

    return json.dumps(cron)

if __name__ == '__main__':
    print get_crontab()
