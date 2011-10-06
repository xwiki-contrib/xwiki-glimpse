#!/usr/bin/env python
from setuptools import setup, find_packages

setup(
      name = 'glimpse-agent',
      version = '1.0.SNAPSHOT',
      description = 'XWiki Glimpse Agent',
      author = 'Fabio Mancinelli',
      author_email = 'fabio.mancinelli@xwiki.com',
      packages = find_packages(),
      install_requires = [
                          'python-daemon >= 1.6'
                          ],
      entry_points = {
                      'console_scripts' : [
                                           'glimpse-agent = glimpse_agent.agent:main'
                                           ]
                      }
      )
