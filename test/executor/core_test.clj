(ns executor.core-test
  (:require [clojure.test :refer :all]
            [ring-http-exchange.core :as server]
            [hato.client :as http])
  (:import (jj.arminio.concurrent ProxyExecutorService)
           (java.util.concurrent Executors)))

(defn handler [req res rej]
  (res {:status  200
        :body    "hello world"
        :headers {}}))

(deftest throttling-executor-http-test
  (let [real-executor (Executors/newCachedThreadPool)
        executor (ProxyExecutorService. (Executors/newCachedThreadPool))
        port 8080
        server-instance (server/run-http-server handler {:executor executor
                                                         :port     port
                                                         :async? true})]
    (try
      (Thread/sleep 100)

      (let [response (http/get (str "http://localhost:" port))]
        (is (= 200 (:status response)))
        (is (= "hello world" (:body response))))

      (let [num-requests 500
            responses (doall
                        (pmap
                          (fn [_]
                            (http/get (str "http://localhost:" port)))
                          (range num-requests)))]
        (is (= num-requests (count responses)))
        (is (every? #(= 200 (:status %)) responses))
        (is (every? #(= "hello world" (:body %)) responses)))

      (finally
        (server/stop-http-server server-instance)
        (.shutdown executor)
        (.shutdown real-executor)))))