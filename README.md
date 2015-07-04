# ZephCore

This plugin forms the basis of many of my plugins.

Features ZephCore provides:

* Abstracted NMS handling - makes it easy to use NMS code by providing a interface and various implementations.
* Executor for scheduler
  * Shades in comphenix's excellent BukkitExecutors library
  * Uses Guava's executor interfaces to make calling of tasks much easier, including callables, futures, and listenable futures
  * Some callbacks to use with system, to fetch a numer of player names from UUIDs, or vice versa
* Implementation of Intake
  * Virtual Intake command manager that wraps and forwards to bukkit
  * Binding for various bukkit types
  * Adds commands to bukkit /help through wrapper of Intake values
* Database Management
  * Abstractified implementation of database
  * Current implementations are MySQL and Mongo
  * Connection pool using BoneCP for MySQL
  * Schema support - automatically upgrade a database
  * Asynchronous execution of non-returning queries
  * Map-based return system
* Simple config system
  * Allows for setting default file, saving defaults, and reading values easily
* Fetching of Names and UUIDs
  * Uses combination of own cache, implementation's cache, or web query
  * **WARNING**: Requests may freeze server thread
* A bunch of utilities used in multiple of my plugins