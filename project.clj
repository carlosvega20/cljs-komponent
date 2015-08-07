(defproject cljs-komponent "0.0.1"
  :description "UI component to create: carousel, panels, tabs, pagination, slider."
  :url "https://github.com/carlosvega20/cljs-komponent"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2760"]
                 [reagent "0.5.0-alpha"]
                 [cljs-ajax "0.3.9"]
                ]

  :plugins [[lein-environ "1.0.0"]
            [lein-cljsbuild "1.0.5"]
            [com.cemerick/clojurescript.test "0.3.3"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {:optimizations :none
                                   :output-to "target/app.js"
                                   ; :source-map true
                                   :output-dir "target/"
                                   }}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "test-results/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]
                        :test-commands {"unit-tests" ["phantomjs" :runner
                                              "test-results/unit-test.js"]}}
  :min-lein-version "2.5.0") 
