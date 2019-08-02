(ns matasano-crypto.conversion
  (:require [clojure.string :as string]))

(defn hexchar->byte
  [digit]
  (let [upper (first (string/upper-case digit))
        digit-cast (- (int upper) (int \0))
        result (if (> digit-cast 9) (+ (- (int upper) (int \A)) 10) digit-cast)]
    result))

(defn hextuple->byte
  "Receives two hexchars and returns corresponding byte (as int)"
  [[hextens hexunits]]
  (let [hextens-int (hexchar->byte hextens)
        hexunits-int (hexchar->byte hexunits)]
    (+ (* hextens-int 16) hexunits-int)))

(defn hexstring->bytes
  [hexstring]
  (let [hex-list (partition 2 hexstring)
        byte-list (mapv hextuple->byte hex-list)]
    byte-list))

(def base64chars "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
(def base64padding \=)

(defn base64-3byte->4chars
  "Converts 3 bytes into 4 base64 chars"
  ([byte1]
   (apply conj (vec (take 2 (base64-3byte->4chars byte1 0 0))) [base64padding base64padding]))
  ([byte1 byte2]
   (conj (vec (take 3 (base64-3byte->4chars byte1 byte2 0))) base64padding))
  ([byte1 byte2 byte3]
   (let [sextet1 (bit-shift-right (bit-and byte1 2r11111100) 2)
         sextet2 (+ (bit-shift-left (bit-and byte1 2r00000011) 4) (bit-shift-right (bit-and byte2 2r11110000) 4))
         sextet3 (+ (bit-shift-left (bit-and byte2 2r00001111) 2) (bit-shift-right (bit-and byte3 2r11000000) 6))
         sextet4 (bit-and byte3 2r00111111)
         chars (mapv #(nth base64chars %) [sextet1 sextet2 sextet3 sextet4])]
     chars)))

(defn base64-3byte->string
  "Converts 3 bytes into a base64 string"
  [bytes]
  (apply str (apply base64-3byte->4chars bytes)))

(defn bytes->base64
  "Converts an array of ints into base64"
  [bytes]
  (let [partitions (partition-all 3 bytes)
        pieces (map base64-3byte->string partitions)]
    (string/join pieces)))

(defn hexstring->base64
  [hexstring]
  (bytes->base64 (hexstring->bytes hexstring)))

(defn xor-bytes
  "XORs two equal-sized byte buffers"
  [buffer1 buffer2]
  (map #(bit-xor %1 %2) buffer1 buffer2))

(defn xor-hexstrings
  "XORs two equal-sized hexstring"
  [string1 string2]
  (xor-bytes (hexstring->bytes string1) (hexstring->bytes string2)))


