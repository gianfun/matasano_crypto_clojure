(ns matasano-crypto.conversion-test
  (:require [midje.sweet :refer :all])
  (:require [matasano-crypto.conversion :as conversion]))

(def hex-string "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
(def hex-in-bytes [0x49 0x27 0x6d 0x20 0x6b 0x69 0x6c 0x6c 0x69 0x6e 0x67 0x20 0x79 0x6f 0x75 0x72 0x20 0x62 0x72 0x61 0x69 0x6e 0x20 0x6c 0x69 0x6b 0x65 0x20 0x61 0x20 0x70 0x6f 0x69 0x73 0x6f 0x6e 0x6f 0x75 0x73 0x20 0x6d 0x75 0x73 0x68 0x72 0x6f 0x6f 0x6d])
(def base64 "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")

(facts "Converts hex char to byte"
      (tabular
        (conversion/hexchar->byte ?char) => ?int
        ?char ?int
        \0 0
        \1 1
        \2 2
        \3 3
        \4 4
        \5 5
        \6 6
        \7 7
        \8 8
        \9 9
        \a 10
        \b 11
        \c 12
        \d 13
        \e 14
        \f 15))

(facts "Converts hextuples to byte"
       (tabular
         (conversion/hextuple->byte ?tuple) => ?int
         ?tuple  ?int
         "00"    0
         "01"    1
         "0f"    15
         "10"    16
         "1f"    31
         "a0"    160
         "aa"    170
         "ff"    255
         ))

(fact "Converts Hex string to bytes"
      (conversion/hexstring->bytes hex-string) => hex-in-bytes)

(fact "Converts triples of bytes to base64 chars"
      (tabular
        (conversion/base64-3byte->4chars ?b1 ?b2 ?b3) => ?base64
        ?b1      ?b2      ?b3          ?base64
        97       97       97           [\Y \W \F \h]
        (int \M) (int \a) (int \n)     [\T \W \F \u]
        (int \s) (int \u) (int \r)     [\c \3 \V \y]
        ))

(fact "Converts two bytes into base64 chars"
      (conversion/base64-3byte->4chars (int \s) (int \u)) => [\c \3 \U \=])

(fact "Converts one byte into base64 chars"
      (conversion/base64-3byte->4chars (int \s)) => [\c \w \= \=])


(fact "Converts triples of bytes to base64 string"
      (tabular
        (conversion/base64-3byte->string ?triple) => ?base64
        ?triple                          ?base64
        [97 97 97]                       "YWFh"
        [(int \M) (int \a) (int \n)]     "TWFu"
        [(int \s) (int \u) (int \r)]     "c3Vy"
        [(int \s) (int \u)]              "c3U="
        [(int \s)]                       "cw=="
        ))

(fact "Converts bytes to base64"
      (tabular
        (conversion/bytes->base64 ?bytes) => ?base64
        ?bytes                         ?base64
        [97 97 97]                     "YWFh"
        [(int \s) (int \u) (int \r)]   "c3Vy"
        [(int \s) (int \u)]            "c3U="
        [(int \s)]                     "cw=="
        hex-in-bytes                   base64
        ))

(fact "Converts Hex string to base64"
      (conversion/hexstring->base64 hex-string) => base64)


