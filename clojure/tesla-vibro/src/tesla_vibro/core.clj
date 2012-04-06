(ns tesla-vibro.core
  (:require (cheshire [core :as ch])
            (http.async [client :as c])))

(defn doit [path]
  (with-open [client (c/create-client)]
    ;; request http resource
    (let [response (c/GET client (str "http://localhost:8001/tesla/" path))]
      (c/await response)
      (ch/parse-string (c/string response) true))))
