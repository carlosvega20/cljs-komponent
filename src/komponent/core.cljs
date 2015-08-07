(ns komponent.core
  (:require [reagent.ratom :as r :refer [atom cursor]]))

;-------------------------------------------------------------------------
; Pagination Component
;-------------------------------------------------------------------------
;-------------------------------------------------------------------------
;--------- State ---------------------------------------------------------
;-------------------------------------------------------------------------

(def pager-state (atom {:current-page 0
                        :records-per-page 5}))
 
(defn change-page! [page]
  (swap! pager-state assoc :current-page page))

(def current-page-cursor (cursor pager-state [:current-page]))
(def records-per-page-cursor (cursor pager-state [:records-per-page]))
;-------------------------------------------------------------------------
;--------- Logic ---------------------------------------------------------
;-------------------------------------------------------------------------

(defn bound-records
  "Return a sequence of bounded and limited records"
  [records offset limit]
  (take limit ( drop (* offset limit ) records ) ))

(defn total-pages-per-view
  "Get the total number of paginated pages"
  [pages-number records-per-page]
  (+ (quot pages-number records-per-page)
  (if (> (mod pages-number records-per-page) 0) 1 0)))

;-------------------------------------------------------------------------
;--------- Views ---------------------------------------------------------
;-------------------------------------------------------------------------

;--------- Factor of Views -----------------------------------------------
(defn paginator
  "Factory function that creates records and a visual paginator"
  [records offset records-per-page]
  (let [resulted-records (bound-records records offset records-per-page)]
    (fn [command callback & args]
      (cond (= command :each) 
        (for [entry resulted-records] (apply callback entry args))
        :else (apply pager-view (count resulted-records) offset callback args)))))
;--------------------------------------------------------------------------

(defn pager-record-view 
  "Render each HTML paginated element"
  [item update-page! current-page]
    [:li {:class (str (if (= current-page item) "active"))}
        [:a {:on-click #(update-page! item)} (+ item 1)]])

(defn pager-page-view 
  "Render paginated Pager (Number links of pages)"
  [pager-records update-page! current-page]
    (let [pager-total-records (count pager-records)
          total-pager-pages (total-pages-per-view pager-total-records @records-per-page-cursor)
          pager-current-page (quot (* current-page total-pager-pages) pager-total-records)
          pager (paginator pager-records pager-current-page @records-per-page-cursor)]
      (change-page! pager-current-page)
      (pager :each pager-record-view update-page! current-page)))

(defn pager-view
  "Render HTML Paginator (The Pager)"
  [total-pages per-page current-page update-page!]
    (let [pages-per-view     (total-pages-per-view @total-pages @per-page)
          prev-thumbs        (* (- @current-page-cursor 1) @records-per-page-cursor)
          next-thumbs        (* (+ @current-page-cursor 1) @records-per-page-cursor)
          prev-disabled?     (<= @current-page 0)
          next-disabled?     (>= @current-page (- pages-per-view 1))]
      [:ul.pagination-sm.navbar-right.pagination
        [:li {:class (str (if prev-disabled? "disabled"))} 
          [:a (if-not prev-disabled? 
            {:on-click  #(update-page! 0)}) "First"] ]
        [:li {:class (str (if prev-disabled? "disabled"))} 
          [:a (if-not prev-disabled?
            {:on-click  #(update-page! (- @current-page 1))}) "Previous"] ]
        [:li {:class (str (if (< prev-thumbs 0) "hide"))} 
          [:a {:on-click #(update-page! prev-thumbs )} "..."] ]
        (pager-page-view (range pages-per-view) update-page! @current-page)
        [:li {:class (str (if (> next-thumbs (- pages-per-view 1)) "hide"))} 
          [:a {:on-click #(update-page! next-thumbs )} "..."] ]
        [:li {:class (str (if next-disabled? "disabled"))} 
          [:a (if-not next-disabled? 
            {:on-click  #(update-page! (+ @current-page 1))}) "Next"] ]
        [:li {:class (str (if next-disabled? "disabled"))} 
          [:a (if-not next-disabled? 
            {:on-click  #(update-page! (- pages-per-view 1))}) "Last"] ]]))
;-------------------------------------------------------------------------
;-------------------------------------------------------------------------
;-------------------------------------------------------------------------