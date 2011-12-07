!SLIDE code
# An example function

    (defn old-peoples-names [people]
      (map #(select-keys % [:first :last])
           (filter #(> (:age %) 80)
                   people)))

!SLIDE code

    (defn records-matching
      [key-field pred goal-keys coll]
      (->> coll
           (filter #(pred (get % key-field)))
           (map #(select-keys % goal-keys))))

    (defn records-matching
      [key-field pred goal-keys coll]
      (for [m coll
            :let [key-val (get m key-field)]
            :when (pred key-val)]
        (select-keys m goal-keys)))

!SLIDE bullets
# Clojars registration

* Need an SSH keypair
* You probably have one already in ~/.ssh
* use `ssh-keygen` if not
* Copy the .pub key to clojars

!SLIDE bullets
# Creating a library

* `lein new amalloy-utils`
* `cd amalloy-utils`
* `emacs src/amalloy/utils.clj` (...edit it...)
* `lein pom && lein jar`
* `scp pom.xml *.jar clojars@clojars.org`
* `cake release`

!SLIDE bullets
# You're done!

* Code is now on Clojars
* Available just like any other library
