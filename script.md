# Motivation

"Utility" - what an overused word in programming! Many projects wind up with a
massive "Utils" class/namespace that does a hodgepodge of unrelated things, just
because it's easier to keep things together than split them up. So why am I
encouraging you to do this? Because programming languages just don't provide all
the tools you want to use when you start a project: they're not supposed to!
They just provide the means needed to express ideas, and a core set of
abstractions and tools. That's why we all use gobs of libraries when we're
starting a new project: to avoid writing them from scratch.

But there are lots of things we do all the time that *aren't* in any popular
library, because they're so small it's easy enough to reinvent them, or they
don't fit into any existing library's "theme". And there are things that you,
personally, do often because of your problem domain or programming style, but
most people don't really care about. So they're not in a library, and when you
start a new project you end up copying and pasting from project Foo into project
Bar in bits and pieces, as you realize you want that tool you invented while you
were working on Foo. For the longest time, I did this myself. But a key
realization is that "library" doesn't have to mean this big scary thing that
lots of people use: it's just a collection of code, bundled up for convenient
reuse. If nobody else ever uses your library, it's a great library because it
does exactly what you personally want.

And Clojure has a dual heritage that is perfect for this: it is on the JVM,
which makes it *amazingly* easy to create, distribute, and use libraries. A huge
amount of time has gone into making Java libraries easy to get a hold of, and we
can benefit from that. Clojure even goes a step further, by having a
free-for-all public Maven repository called Clojars - getting your stuff there
is a piece of cake. And it is a Lisp, which encourages bottom-up programming and
constructing general-purpose tools so that you can "build the language up" to
your program.

# An example

So let's look at an example. Let's say you need to work with sequences of
hashes, and you want to get some subset of fields from each hash that meets some
predicate. So you glue something together with filter, map, and select-keys, or
maybe with a `for` list-comprehension instead.

(defn old-peoples-names [people]
  (map #(select-keys % [:first-name :last-name])
       (filter #(> (:age %) 80)
               people)))

Great, but it doesn't capture the abstraction, and you're going to do something
like this in several places, so you pull it out into a function:

(defn records-matching [key-field pred goal-keys coll]
  (->> coll
       (filter #(pred (get % key-field)))
       (map #(select-keys % goal-keys))))

;; OR ;;

(defn records-matching [key-field pred goal-keys coll]
  (for [m coll
        :let [key-val (get m key-field)]
        :when (pred key-val)]
    (select-keys m goal-keys)))

Pretty small function, right? But it's already large enough where you don't want
to write it (and test it) again if you can help it; and it's general enough that
you can easily imagine reusing it in a different project. So put it in your
utility library once, and you can use it from any project you ever work on!

# Clojars registration

By this time hopefully I've convinced you that utility libraries are good, and
you're wondering when I'll get on with what I claimed would be my topic tonight:
Clojars. I will, and there's good news on that front: Clojars is *so easy* I
couldn't make a whole talk out of it.

## Detour: SSH keys

The most complicated step is one that many of you won't even need to do, because
you already have this set up: you need a public/private SSH keypair, just like
you do in order to push to Github. Clojars asks for your public SSH key to
authenticate you, and then from any machine where you have your private SSH key,
you can push jars up with SCP. On a Unix machine (Mac or Linux), you can just
run ssh-keygen, accept the default file locations, and make up a passphrase for
your key (optional). Tada, now you have a private key named id_rsa and a public
key named id_rsa.pub (or similar). NEVER share your private key, but you can
freely share your public key, as we're about to with Clojars.

## Registration

So if you've never released to Clojars before, the first thing you need to do is
create a user account for yourself. I already own the amalloy user, and it's a
little complicated to use multiple Clojars accounts on a single client machine,
so I won't create a new user. But you can just go to clojars.org, click on
Register, and pick a username. It wants a public key so it can identify you, so
just copy the contents of id_rsa.pub and send it up there.

# Creating a library

The first thing you have to do is create a Leiningen (or Cake) project to
contain your library. You can come up with a clever name if you want, or not -
when I started mine I just called it amalloy-utils. So $ lein new
${YOUR_NAME}-utils will create a project that looks pretty reasonable. Let's
copy that function from earlier and put it somewhere reasonable in the source
tree - I like src/yourname/utils.clj for now, so let's rename core.clj to
utils.clj and fix up the namespace declaration.

## Pushing to Clojars

Now that we have this great library, it's time to release it into the wild. Cake
users have it easy here - cake has a built-in "release" command that you could
run right now and it will do the necessary negotiation with Clojars. But it's
not hard in lein either. Just run `lein pom` to transform your project.clj into
a "pom file" describing your library, and `lein jar` to create a jar containing
your code. Then:

    $ scp pom.xml *.jar clojars@clojars.org

That's it! Clojars will ask your SSH client to authenticate, and it'll figure
out what your clojars username is; then it will create a directory for storing
your stuff and copy it on up. At this point, your code is now available at
clojars.org/YOURNAME-utils. You can repeat the pom/jar/scp step whenever you
want to upload a new version, although you should usually also change the
version number so that you don't overwrite already-released code.

## Using your library

So how do you use your library from a real project? Just add a dependency to it
in project.clj, just as you would for any other library, such as Compojure.

    (defproject my-awesome-project "0.5.0"
      :dependencies [[clojure "1.3.0"]
                     [YOURNAME-utils "0.2.3"]])

Now you can load your library namespaces into another namespace and use those
functions. Keep in mind that the namespace name here depends not on the name of
your utility library, but on what namespace you put them into inside of that
library.

    (ns my.awesome.project
      (:use [YOURNAME.utils :only [records-matching]]))

    (defn old-peoples-names [people]
      (records-matching :age #(> % 80) [:first-name :last-name] people))

# Useful

These days I don't use amalloy-utils - it has merged with Useful, Geni's
open-source utility library. Justin helped me really polish several of the neat
ideas I had put into amalloy-utils, and I helped him organize the huge blobby
mess Useful had become. You're welcome to use Useful, and send us pull requests
if you want - a few people have. But the whole point is you don't have to - you
can have your own library, and we've refused several pull requests because the
functions added therein just don't interest us, useful as they might be. Nobody
will stop you from putting them in your own library, though!

## A tour

So I've pulled a few sample functions from Useful that I think demonstrate the
sorts of things a utility library is good for. I've ordered them sorta from
"easiest to understand and most interesting" to "hard to understand or boring",
so we'll see how many of these I get through before I'm tired of talking or
you're tired of listening - just throw tomatoes or something, the slides will be
online anyway if you want to follow up later.

### find-first

The easiest one here is our `find-first` function. Super-easy to write and
understand, and adds a function that you might be surprised to learn isn't in
clojure.core. Just, given a predicate and a collection, return the first item in
the collection that satisfies the predicate. Almost like `some`, but as you can
see here `some` returns the *predicate's result*, not the item.

### ?

The question mark is a super-handy debugging macro. If you're wondering what's
going on at some point in your code, just wrap an expression like (? (range 3)). It
won't change the value of the expression at all, but when that expression is
evaluated it will be printed to standard out, like "(range 3) is (0 1 2)". The
implementation could have been simple, but it's kinda cluttered up so that it
handles exceptions usefully and we can write a ?! version that writes to a file
instead of standard out. If you want you can see the original, simple version in
[amalloy-utils](https://github.com/amalloy/amalloy-utils/tree/master/src/amalloy/utils/debug.clj).

### lazy-loop

This one's a useful macro for building lazy sequences - it lets you write lazy
sequences as if they were loop/recur forms, but you can recur from any position
rather than just tail position. This magic is possible because loop/recur is
more general: lazy-loop only works for creating lazy sequences. So for example,
here's how you could write a simple version of `range` yourself with
lazy-loop. Look how nicely it flows compared to a version without lazy-loop,
where you have to do things in this weird order of args/body/init, and invent a
range* function as well as inserting a boilerplate `lazy-seq`.

### fix

Fix is a function that's a little hard to describe in words. Basically it takes
an input value and a sequence of predicate/transformer pairs - for the first
predicate that matches the input, the resulting transformer is called on the
input. There's also to-fix, which takes the same arguments without the actual
input value - instead it returns a function that takes an input value and does
the matching and transforming later. This is great for, for example, mapping
"fix-functions" over a sequence

### thread-local

Thread-local is a wrapper around Java's horribly painful ThreadLocal class: it
lets you write an expression that will get executed once for each thread that
wants it. The most common use is for giving each thread its own private copy of
a thread-unsafe object like a Java TextFormatter, but you can also use it to
give threads a unique separate value, say a "thread id" as here.

### keyed

Keyed is in a sense the opposite of map destructuring. Instead of creating local
bindings out of a map, it creates a map out of named local bindings.

### knit

Knit is a sibling to juxt, which of course makes it kinda complicated to
explain. Juxt takes N functions and M arguments, and calls each function with
all the arguments; knit takes N functions and N arguments, and calls each
function on only the corresponding argument. The main use-case here is
two-argument knits for transforming key/value pairs in a hashmap.

### update-each

Clojure's core has an `update-in` function for modifying the contents of a
nested hashmap, but it's a bit awkward to apply the same function to multiple
keys at the top level: say you got a map from some JSON input, and you want to
parse the id and count fields as integers. Update-each does that.

# Conclusion

I'm not saying that you should love all of these functions - I'm just trying to
give you a taste of the kinds of things you can generalize once and never
rewrite by putting them into your own utility library. So get out there and
write some dang code!

### TODO

Find someplace to work in some of PG's prose from On Lisp.

Specifically:

* Warring nations, evolving together
* Recognizing missing abstractions
