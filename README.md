Introduction
============

Glimpse is a configuration data collection tool for server infrastructures. Data are sent periodically by agents and stored in XWiki. Agent pages are updated on demand.

Several glimpses can be created: a glimpse gives a partial view on the collected data and allow the user to navigate and find information more efficiently.

Installation
------------

* Build Glimpse using maven (`mvn install`)

* Copy the xwiki-glimpse-server jar into the WEB-INF/lib directory of your XWiki deployment (it will be generated under `xwiki-glimpse-application/target`)

* Add a `&lt;mapping resource="glimpse.hbm.xml"/&gt;` entry in the `WEB-INF/hibernate.cfg.xml` file of your XWiki deployment

* Restart XWiki

* Import the Glimpse application XAR (it will be generated under `xwiki-glimpse-application/taget`)

* Glimpse will be accessible from the `WebHome` pages of the `GlimpseCode` and `Glimpse` spaces.

