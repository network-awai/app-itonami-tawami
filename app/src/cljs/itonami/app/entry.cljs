(ns itonami.app.entry
  "Tawami on the device (AWAI-3502). Calls tawami.methods.analyze unchanged:
  a flexibility map only (no dispatch, no trade). The seed loader's file I/O is
  not used; assets are typed in. Example assets are from the repo's seed."
  (:require [itonami.app.run :as run]
            [tawami.methods.analyze :as a]))

(def ^:private example-assets
  "{:assets
 [{:id \"batt-community-01\" :name \"Community battery (grid-tie)\" :resource-class :battery
   :shiftable-kw 500 :duration-min 120 :availability 0.9 :response-time-min 1
   :advance-window-h 4 :defer-window-h 4 :sourcing :authoritative}
  {:id \"ev-depot-01\" :name \"EV delivery-fleet depot charging\" :resource-class :ev-fleet
   :shiftable-kw 600 :duration-min 240 :availability 0.75 :response-time-min 5
   :advance-window-h 6 :defer-window-h 8}
  {:id \"ind-arc-furnace-01\" :name \"Arc furnace batch shift\" :resource-class :industrial-process
   :shiftable-kw 2000 :duration-min 60 :availability 0.5 :response-time-min 90
   :advance-window-h 2 :defer-window-h 2}]}")

(run/register!
 {:name "analyze" :label "柔軟性を見積もる"
  :help "資産ごとに応答の速さ・ずらせるエネルギー・時間の幅・柔軟性の価値・区分（fast / mid / slow）を計算し、資源の種類ごとと全体の合計を出します。"
  :example example-assets
  :run (fn [{:keys [assets]}] (a/analyze assets))})

(run/register!
 {:name "report" :label "柔軟性マップを作る"
  :help "同じ計算から、Markdown の柔軟性マップと資源の種類ごとの網羅状況を作ります。"
  :example example-assets
  :run (fn [{:keys [assets]}] (a/render-report (a/analyze assets) (a/coverage assets)))})
