# Point Of Sale Exercise

This is an exercise using jHipster to develop a Point Of Sale web application.

You can view this app in action [here](http://foobar.cf/pos) thanks to these free resources:

* [Nixiweb](http://www.nixiweb.com) (my personal domain for static hosting)
* [Heroku](https://www.heroku.com/) (application server hosting)
* [Amazon Web Services](https://aws.amazon.com/free/) (postgres database hosting)

# Some Features

a. Bilingual interface (English & Spanish)

b. 2 Level Dropdown Menu in admin's back-office for this Entities:

* _Cashiers_ (Users with **ROLE_CASHIER** Profile)
* _Taxes_
* _Percentage Discounts_
* _Payment Methods_
* _Products_
* _Shops_
* _Point Of Sales_ (Active / Inactive)
* _Turns_ (A _Cashier_ Active / Inactive in a _POS_)
* _Catalogs_ (barcode database for every _Shop_)
* _Sales_
* _Items_
* _Tax Summary_

c. Client side integrity checking

* For barcode minimum length
* Check minimum amount to pay after select _Payment Method_.

d. Server side integrity checking

* Barcode must exists in _Shop's_ _Catalog_. Else, search in [upcdatabse.org](http://upcdatabase.org/random) external barcode repository and creates in internal _Catalog_ database with random _Price_ and _Discount_
* Credit Card payment error random simulation

e. All calculations made in Server side

* Current _Sale_ and _Items_ saved in real time
* _Tax Summary_ saved only before Payment
* Reload last uncompleted _Sale_ after _Turn_ login

f. Print _Shop_ and _Cashier_ active _Turn_ info in screen and ticket

g. Agile Workflow

* EAN13 Scan ready: autocomplete without lost focus
* 3 input methods: Key shortcuts capture / Tactile screen / Mouse pointer
* Negative quantities for corrections with adjustment trace

h. Bonus:

* Draw last introduced barcode chart in screen
* Scan, Keyboard and Dot Matrix Printer sounds
* Automatic adjust screen elements size to screen resolution
* Hide / Show Menu to get some more work space
* Show Barcode popup on mouse over _Items_

# Some Screenshots

![POS Main Page]
(http://foobar.cf/pos/pos_main.png)

![POS Payment Page]
(http://foobar.cf/pos/pos_payment.png)

![POS Print Invoice Page]
(http://foobar.cf/pos/pos_printticket.png)