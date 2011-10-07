#!/usr/bin/env python
import MySQLdb # This plugin requires Python MySQLdb module
import json

###################################################
# CONFIGURATION
###################################################
USERNAME = 'root'
PASSWORD = 'root'
###################################################

def get_mysql_info():
    try:
        db = MySQLdb.connect(user = USERNAME, passwd = PASSWORD)
    except Exception as e:
        return json.dumps({'error' : str(e)})

    cursor = db.cursor()
    cursor.execute('select version();')
    version = cursor.fetchone()[0]

    replication = 'off'
    replication_status = 'not available'
    cursor.execute('show slave status;')
   
    if cursor.rowcount > 0:
        replication = 'on'
        colname = [d[0] for d in cursor.description]
        row = cursor.fetchone()
        if row[colname.index('Slave_IO_Running')] == 'Yes' and row[colname.index('Slave_SQL_Running')] == 'Yes':
            replication_status = "running"
        else:
            replication_status = "error"
    cursor.close()

    info = {}
    info['Version'] = version
    info['Replication'] = replication
    info['Replication status'] = replication_status

    return json.dumps(info)

if __name__ == '__main__':
    print get_mysql_info()
