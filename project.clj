(defproject org.clojars.jj/arminio "1.0.0-SNAPSHOT"
  :description "An experimental Executor that uses virtual threads to isolate blocking operations, ensuring the underlying ExecutorService only handles non-blocking coordination while blocking code executes in virtual threads"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]

  :profiles {:test {:dependencies [[org.clojars.jj/ring-http-exchange "1.3.0"]
                                   [hato "1.0.0"]]}}

  :java-source-paths ["src/java"]
  :source-paths ["src/clojure"]
  :repl-options {:init-ns executor.core}

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]


  :plugins [[org.clojars.jj/bump "1.0.4"]
            [org.clojars.jj/bump-md "1.1.0"]
            [org.clojars.jj/lein-git-tag "1.0.0"]
            [org.clojars.jj/strict-check "1.1.0"]]

  )
