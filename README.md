# Scorigami
This project is a rugby scorigami bot.

## What is a scorigami?
Coined by [Jon Bois](https://en.wikipedia.org/wiki/Jon_Bois), scorigami is a scoring combination that has never happened before in a sport or league's history. The term usually refers to american football scores, and NFL scores in particular. This project introduces the concept to the great ancestor of american football - Rugby union.
You can learn more about scorigami [here](https://en.wikipedia.org/wiki/Scorigami) and [there](https://www.youtube.com/watch?v=9l5C8cGMueY).

## What does the bot do?
The bot fetches the latest score from an [online sports database](https://www.thesportsdb.com/) using its API, checks if the score is a scorigami and tweets accordingly [in its twitter account.](https://linktr.ee/Scorigami)

## Where can I follow the bot?
[Everything you need is here.](https://linktr.ee/Scorigami)

## What leagues are supported?
Currently the bot tracks [MLR](https://en.wikipedia.org/wiki/Major_League_Rugby) and [Six Nations](https://en.wikipedia.org/wiki/Six_Nations_Championship) scores. However, the code itself is not limited to a specific league. Given the data, it can support every rugby union competition.

## How far back does the data go?
The data dates back to each league's inaugural season.
* ###MLR - April 2017.
* ###Six Nations - December 1882.

## What is the source of the data?

###MLR
All the data was available at [TheSportsDB.](https://www.thesportsdb.com/)

###Six Nations
Only a few seasons of data were available at [TheSportsDB.](https://www.thesportsdb.com/), the rest is from Wikipedia.

## Note
The Six Nations data dates all the way back to December 1882, it starts from the days of the Home Nations to Five Nations and then to Six Nations, and hopefully sooner rather than later a [Seven Nation Army](https://www.youtube.com/watch?v=0J2QdDbelmY). Not only the name, the format and the participants have changed since the early days, but also the scoring code of rugby has changed several times, meaning some of the scores in the database are impossible to reprise under today's rules.

## Packages used
   *[twitter4j](https://twitter4j.org/en/index.html)
 
## Languages
   *Java
   
## API used
   *Twitter
   *[TheSportsDB](https://www.thesportsdb.com/)

