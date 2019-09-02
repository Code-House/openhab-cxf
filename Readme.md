# OpenHab CXF Integration

Currently OpenHab uses Jersey to launch REST services and custom code to glue things together which attempts to use osgi
whiteboard pattern for [gluing everything](https://github.com/hstaudacher/osgi-jax-rs-connector/) together. While way it
is done currently have benefits it have also big drawbacks such no way to configure service layer security (built into CXF),
more over project seems to be not maintained any longer. To solve some of these troubles I provided adapter which uses REST
resources registered as services and publish them in one CXF endpoint (see [cxf](./cxf) module).

There are ongoing troubles with few things. For example [swagger](http://github.com/Code-House/karaf-swagger) integration currently doesn't work.

Integration test is failing, please ignore it. This feature set will not work on plain karaf due to implicit eclipse smart home dependencies.

Requires
---
Karaf 4.2.6 (OpenHab 2.5.0 (M2))

Provides
---
CXF powered openhab rest layer.

## Developer introduction

By default openhab distribution does not use local maven repository nor any other repository. To install CXF you need to
enable it by the hand. Please edit `runtime/karaf/etc/org.ops4j.pax.url.mvn.cfg` file to enable local repository resolution
and remote repositories so CXF dependencies can be resolved during installation time.

To start using openhab cxf services in your project you need to install karaf feature set:

```
feature:repo-add mvn:org.code-house.openhab/features/0.8.0-SNAPSHOT/xml/features
feature:install jackson-jaxrs-json-provider
feature:install openhab-cxf
```


