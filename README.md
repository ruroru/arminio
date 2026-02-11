# arminio

A Clojure library designed to isolate blocking of caller in a Java virtual thread, allowing main executor to focus on CPU-bound tasks.

## Installation

Add the following dependency to your `project.clj`:

```clojure
[org.clojars.jj/arminio "1.0.0-SNAPSHOT"]
```

## Usage

### Basic Setup

```clojure
(def executor (ProxyExecutorService. (Executors/newFixedThreadPool 10)))

;; Start server with the executor
(def server-instance
  (server/run-http-server handler
                          {:executor executor
                           :port     8080
                           :async?   true}))
```

## Requirements

- Java 21+ (for virtual threads)

## License

Copyright © 2026 [ruroru](https://github.com/ruroru)

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.