Play Coffee React sbt plugin
============================

play-coffee-react is an sbt plugin, which utilizes cjsx (linkt ot cjsx) to compile JSX assets,
written in CoffeeScript, under the Play framework. 

Usage
=====

Currently the plugin is under development and not published. There are two possible ways, if You
want to participate in the development or use it:


## Local

1. Fork or clone it to your local filesystem.
2. Add the following to Your plugins.sbt:

```scala
lazy val root = project.in( file(".") ).dependsOn(assemblyPlugin)
lazy val assemblyPlugin = uri("file:////path/to/play-coffee-react")
```

3. In Your play project directory issue:

```bash
$ sbt reload
```

Note that every time You make a change to the plugin, You have to repeat step 3.

## Via github

1. Add the following to Your plugins.sbt:
   
```scala
lazy val root = project.in( file(".") ).dependsOn(assemblyPlugin)
lazy val assemblyPlugin = uri("file:////path/to/play-coffee-react")
```

2. In Your play project directory issue:

```bash
$ sbt reload
```