# clj-geni

A simple Clojure client for the [Geni](http://geni.com) API.

## Usage

The Geni API is simple and consistent enough that we really only need
one function: `api` for reading and writing via the API. Here is how we
use the API's write methods:

```clojure
user=> (require 'clj-geni.core)
nil
user=> (def token "youroauthaccesstoken")
#'user/token
user=> (clj-geni.core/api "/document/add" {:text "foo bar baz", :title "baz bar foo", :labels ["foo" "bar" "baz"] :description "A foo of the bar and the baz." :date "03-12-1733" :token token} :post)

{:labels ["baz" "bar" "foo"], :content_type "text/plain", :date {:day 12, :month 3, :year 1733}, :sizes {:original "/images/documents/doc.png", :large "/images/documents/doc.png", :url "https://www.geni.com/api/document-747358", :thumb "/images/documents/doc.png"}, :updated_at "1332972988", :guid "6000000016025259023", :title "baz bar foo", :created_at "1332972988", :url "https://www.geni.com/api/document-747358", :id "document-747358", :description "A foo of the bar and the baz."}

user=> (clj-geni.core/write "/document/add" {:text "foo bar baz", :title "baz bar foo", :labels ["foo" "bar" "baz"] :description "A foo of the bar and the baz." :date "03-12-1733" :token token})

{:labels ["baz" "bar" "foo"], :content_type "text/plain", :date {:day 12, :month 3, :year 1733}, :sizes {:url "https://www.geni.com/api/document-747360", :large "/images/documents/doc.png", :thumb "/images/documents/doc.png", :original "/images/documents/doc.png"}, :updated_at "1332973005", :guid "6000000016025118014", :title "baz bar foo", :created_at "1332973005", :url "https://www.geni.com/api/document-747360", :id "document-747360", :description "A foo of the bar and the baz."}
```

We have two options: `write` or the slightly lower-level `api` function.
The difference is that you don't have to pass :post to the `write`, you
must do so for `api`. There is also a `read` function for reading with a
GET request.

Let's note a few things:

* For the `:labels` parameter, we passed a vector of strings. clj-geni
  will convert this to a string of comma-separated values. For example,
  in this case, the `labels` param will be set to `foo,bar,baz`.
* We passed a token key with our token. This will be passed to the Geni
  API as the `access_token` parameter. It is your oauth access token
  that you must get yourself.

Here are some example reads:

```clojure
user=> (clj-geni.core/read "/profile-101" {:token token})
{:public false, :name "Amos Elliston", :id "profile-101", :guid "1"}
user=> (clj-geni.core/read "/profile-101" {:fields ["name" "public"] :token token})
{:public false, :name "Amos Elliston"}
```

## Getting an Access Token

Steps to get an access token for testing:

* Register a new app at http://www.geni.com/platform/developer/apps/new
* Visit the API explorer at http://www.geni.com/platform/developer/api_explorer
  and click 'Get Access Token'.

This token will be valid for reads and writes as long as the token is
tied to your application (you can ensure this is true by making sure
that your app is selected in the dropdown on the left of the access
token field on the explorer page.

## License

Distributed under the Eclipse Public License, the same as Clojure.
