# lco
LCO Assignment

## Task
Make a search view like "Places" tab.

It should have the following functions:
* Search function using a search bar
* Category filter function
* Sort functions
	- updated date
	- sort by alphabet
	- sort by rate

Each cell should have:
* name
* category name
* star rating
* picture
	
When you develop this test app, please use SQLite and Git.	

## Android Components
A single Activity with 4 Fragments.
 
Using SQLite, I have 2 database tables PLACES and CATEGORIES.

CATEGORIES
* ID (INTEGER)
* NAME (TEXT)

PLACES
* ID (INTEGER)
* NAME (TEXT)
* RATING (INTEGER)
* IMAGE_PATH (TEXT)
* UPDATED (LONG)
* CATEGORY_ID (INTEGER)

## App Screenshots
<p align="center">
  <img src="https://github.com/janicetan12/lco/blob/master/images/home.png?raw=true" width="300"/>
  <img src="https://github.com/janicetan12/lco/blob/master/images/places.png?raw=true" width="300"/>
</p>

![Home Tab](https://github.com/janicetan12/lco/blob/master/images/home.png?raw=true)
![Places Tab](https://github.com/janicetan12/lco/blob/master/images/places.png?raw=true)
![Places - Sort](https://github.com/janicetan12/lco/blob/master/images/places_sort.png?raw=true)
![Places - Filter](https://github.com/janicetan12/lco/blob/master/images/places_filter.png?raw=true)
![Places - Search](https://github.com/janicetan12/lco/blob/master/images/places_search.png?raw=true)

## Acknowledgments
ButtonBar - Material Design Bottom Navigation https://github.com/roughike/BottomBar

LoremFlickr - Placeholder Images http://loremflickr.com/

Material Icons - https://design.google.com/icons/
