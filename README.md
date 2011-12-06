Introduction
============

Glimpse is a configuration data collection tool for server infrastructures. Data are sent periodically by agents and stored in XWiki. Agent pages are updated on demand.

Several glimpses can be created: a glimpse gives a partial view on the collected data and allow the user to navigate and find information more efficiently.

Installation
------------

* Build Glimpse using maven

* Copy the xwiki-glimpse-server jar into the WEB-INF/lib directory of your XWiki deployment

* Add a &lt;mapping resource="glimpse.hbm.xml"/&gt; entry in the WEB-INF/hibernate.cfg.xml file of your XWiki deployment

* Restart XWiki

