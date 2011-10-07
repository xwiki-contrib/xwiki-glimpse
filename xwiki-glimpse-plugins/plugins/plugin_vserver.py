import os
import glob
import json

def get_vserver_info():
    result = {}

    if os.path.exists('/proc/virtual'):
        f = open('/proc/virtual/info', 'r')
        for line in f:
            if line.startswith("VCIVersion"):
                result['Version'] = line.split(":", 1)[1].strip()
                break
        f.close()
    else:
        result['Version'] = 'No vserver'

    if os.path.exists('/proc/virtual'):
        nodes = []
        for d in glob.glob("/proc/virtual/*"):
            if os.path.isdir(d):
                nsproxy = os.path.join(d, 'nsproxy')
                if os.path.exists(nsproxy):
                    f = open(nsproxy, 'r')
                    for line in f:
                        components = line.split(':', 1)
                        if len(components) == 2 and components[0] == 'NodeName':
                            nodes.append(components[1].strip())
        result['Nodes'] = ','.join(nodes)
    else:
        result['Nodes'] = 'No vserver'

    return json.dumps(result)


if __name__ == '__main__':
    print get_vserver_info()
