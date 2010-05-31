(ns clork
  (:use world))

;; represent a room
;; represent a set of rooms
;; represent monsters

(def direction-desc {:n "North" :s "South" :e "East" :w "West"})

(defn desc-exits [room]
  (let [exits (keys (:exits room))
        direction-strings (sort (map #(% direction-desc) exits))]
    (reduce print-str direction-strings)))

(defn look [world player]
  (let [curr-room-name (get-in world [:players player :location])
        curr-room (get-in world [:rooms curr-room-name])
        room-desc (:description curr-room)
        items-in-room (:items curr-room)
        item-descs (sort (map #(get-in world [:items % :description]) items-in-room))]
    (str (println-str room-desc) 
         (println-str "Exits:" (desc-exits curr-room))
         (println-str "Items:" (reduce print-str item-descs)))))

(defn move-player [world player direction]
  (let [curr-room (get-in world [:players player :location])
        routes (get-in world [:rooms curr-room :exits])]
    (if (contains? routes direction)
      (update-in world [:players player] #(merge % {:location (get routes direction)}))
      world)))

(defn get-items [world player]
  (get-in world [:players player :items]))

(defn current-room [world player]
  (let [player-location (get-in world [:players player :location])]
    (get-in world [:rooms player-location])))

;; needs curr-room stuff done better fails tests.
(defn has-item? [item room]
     (some #{item} (:items room)))

;; move curr-room to has-item? or factor out to get room from tag
(defn add-to-items [world player item]
  (let [curr-room (current-room world player)]
    (if (has-item? item current-room) (update-in world [:players player :items] #(conj % item))
        world)))

(defn remove-from-world [world item])

(defn pick-up [world player item]
  (add-to-items world player item))

;; (doseq [in (repeatedly #(read-line)) :while in] (process-input in))
