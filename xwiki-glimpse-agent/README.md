XWiki Glimpse Agent
===================

This is the Glimpse agent which collects information coming from the probes and send it to the XWiki Glimpse server at regular intervals.

Installation
------------

In order to install the Glimpse server you need the following packages installed on your Debian/Ubuntu system (or equivalent):

* python-setuptools

Once you have installed this dependency, just run setup.py install and you will have the Glimpse Agent installed in your system.

You can specify the --user switch to perform a local install in your home directory.

To start the Glimpse Agent use the Glimpse-agent script. It will be installed in different locations depending on the type of installation you chose (look at installation output to determine the exact location) 

Configuration
-------------

Configuration files can be put in the following directories:

* /etc/glimpse/agent.cfg

* ~/.glimpse/agent.cfg

The file has the following structure:

	[agent]
	option1=value1
	option2=value2
	...
	
The currently supported options are:

* **server**: the Glimpse server to which the agent will send data to. The value has the form of ip_address:port (default: 127.0.0.1:7890)

* **update_interval**: the number of seconds between each data collection (default: 60)

* **probes_dirs**: a colon separated list of directories containing the probes to be executed. (default: /etc/Glimpse/probes:~/.Glimpse/probes)

Probes
------

A probe is an executable file that starts with the 'probe_' string. A probe can be written in any language but must follow some rules in order to interoperate correctly with the Glimpse agent:

* The name of the probe is its filename without the 'probe_' prefix (e.g., probe_test will be named test)

* The probe must write on the standard output all the information it want to communicate to the Glimpse server, using a JSON format

* The exit code will determine the status of the probe

Probes directories are scanned each time, so any change (i.e., adding new probes, modifying the existing ones) will be taken into account at runtime.	
	
If a probe doesn't output valid JSON data then the output is converted to a JSON having the following structure:

	{ "error" : "Probe output not in JSON format", "output" : the actual output of the probe }
