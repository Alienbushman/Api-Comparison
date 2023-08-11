# API comparison
This project highlights the main differences between a couple of framework differences with regards to API's

## General purpose
This just highlights what is the strongest point and what are the weakest points of several commonly used API's

## API features
These are a list of features that I felt are generally required of most API systems and are a good indication of how easy it is to work with a system. The idea was to make it as language-agnostic as possible.

- password: Send in a password
- Session id: To be able to sign in with a password and then to be handed a token with which you use to use the services
- get: get something from a server (here I also played around with some of the local features of the framework)
- post: send in multiple fields for a session
- return json: be able to return information from an Api in a json format (to be language agnostic)
- Send in generic json: The ability to process json in any format (so that you can potentially send in a bunch of unnecessary information and that it would only retain the needed bits)
- Integrate with swagger (or equavalent): This I feel is nessasary for any type of scale or you are spinning up microservices.
- Set the port to a unique port
- Make so that the server can run in a production environment (flask, specific)
- Be sure that the microserveses can be called from different requests

Common features not implemented
The main thing I did not implement was DB's and the reason for that is it makes the code significantly harder to spin up (all the current api's are very simple to ship using either gradle, pip or most IDE's would handle it for you, I did not see an easy way to get a DB running agnostically on every machine without going the docker route, which comes with its own pains)
- Individual password (storage problem)
- Get with individualised URL's (if you can send in variables and they are there with a few of the frameworks, but you have at least the same amount of functionality by being able to send in variables)
- Timed session management (Deciding on a time to expire and where to implement it is a can of worms, although it should not be too difficuilt ot edit the session id logic to add a time to expire element and then either check it on login or as a scheduled job)

## Configs
Ports
- FastApi: 8123
- flask: 5002
- Micronaught: 8888
- Spring: 8008

## Findings between them
### Dynamic vs statically typed languages
It is probably not fair to compare these frameworks, since java is statically typed and python is dynamically typed.
This led to by far the largest difference between the languages being how well swagger worked with the frameworks, because java is statically typed, for most API calls you would send in an object with a fairly precise description of what the json structure would look like. Although you can make an example object with python and structure it out, it is clear that it is added as an afterthought and it required a noticeable amount of effort to get something that is very useful for writing code at scale and maintaining.

### Spring
"A jack of all trades is a master of none, but oftentimes better than a master of one."
 This pretty much describes my experience with spring, there aren't that many niche features to it, and it is very middle of the road on most of its features, but it has wide community support and there didn't seem to be anything that I struggled with in terms of trying to implement. 
There were a couple of nice implementations that have definitely been done better in other languages, but for all my requirements I was able to google my way into a correct solution and it played nice with all my requirements.

### Micronaught
"A knight whose armor is shining hasn't been into battle"
The framework is set up incredibly well, it is very well designed and most of the implementations make a lot of sense, especially if you have tried out a few other frameworks. Everything is integrated well, it really is written in a really intuitive way, and it looks like they know exactly which features are the most commonly used and which people would like to tweak. Also, the Cli for generating the project was the easiest to work with. Although if you get stuck, it is near impossible to get any community help, the error messages are as cryptic as they get, if something is not in the official documentation it is near impossible to fix it. I cannot emphasise how enjoyable the framework is on its own, but right now there is a bug in the code, where if you use the requests library and call it, it throws nasty error messages (but if I call it from either python or I make the same request to the spring version it works fine). I really think with some community love it can turn into something really special.

### FastApi
"If you want to go fast, go alone. If you want to go far, go together."
For the purpose of a small project like this, it is quite difficult to say that FastApi is not the best solution. It has the inherent advantages of python (a lot of tutorials, the syntax is easy and it is quick to write boilerplate code) and it is really modern, so it knows exactly what it wants to be, and the documentation is excellent. 
At this stage, I would say that I have 3 minor gripes with it, but in general, for anyone who wants to set up a small project, it is very difficult not to make the case for FastApi.
 - No out-of-the-box POJO equivalent: The description for the payload in swagger is a little unintuitive and that seems like a feature which was it was not originally designed for, being able to describe a json package precisely is only really needed on larger systems, but I feel like it is one of the basic requirements of an API, since this will be needed when building larger projects
 - Unicorn config: I found it a little jarring to work with the unicorn config and needing to run the system with command lines, it would have been nicer to be able to just run the program with a main.py and then specify the details in the config.
 - Uniqueness: Although it aces most of its features, this was the last API I tackled, and it does not feel like the other API's. It is difficult to give exact examples, but I felt like someone went through painstaking efforts of creating a very feature-complete API and then implemented it, rather than borrowing from other existing frameworks. Although this is also exactly how I would describe how python was constructed. I liked it, but it was the only framework where I felt like I needed to consult the documentation when I was working on something related for some reason (while for the rest, I felt like I only needed to get the basic syntax and config, then the rest would fall into place).

### Flask
"I just kept crawling and it kept working"
This was my introduction to API's back in 2018, so this has clouded my experience with it. The purpose of this project was to start from scratch with each framework and to get a newcomers experience, so I felt like I knew how everything was set up and configured, but when doing it, there was a decent amount of functionality that is no longer supported. So there are new versions for basically anything, but it does require a bit of staying on top of it and what is the latest change. The system works, but it isn't as low bar to entry as it use to be and I feel like it really is feeling the weight of coming into the game being as lightweight as possible and then needing to maintain and grow. It still works very well for what it does and once you have a good ecosystem going you can easily isolate it, however, it is having the same growing problems as React, while it is clear that newer systems like FastApi and Micronaught were designed after the standard for what exactly you want from an API, makes it a lot easier to get going.

## Future work
#### Frameworks to be implemented
 - Gin (Go)
 - Guzzle (php) 
 - Django (python) (Since Django is fundamentally based on a batteries-included philosophy it is quite difficult to just mock easy elements of it, so I have mixed feelings about adding it to the project)
 - add openapi to the python frameworks to make the swagger experience better

#### Requests
When it comes to the requests to do the testing, I just used the standard package for the language, it feels like it might be worthwhile to go back into each framework and see how they write it internally. 
The reason this did not take a higher priority with me is that I feel like the beauty of an API is that it is environment agnostic and it should be evaluated with whatever you call it from

#### Testing
Yeah on pet projects this is always something that can be added.
Although something that does get me is that I did call most of the projects from different API calls to make sure the features were mostly the same (with around 3 exceptions). So it should be easy to just formalise the process to test it all in one, although I did not know what was the best way to set up an independent testing facility that is language agnostic, it is an interesting idea, so I really hope I will revisit it.

## Conclusion
Each framework has its own little quirks, but they mostly work the same. There seem to be 4 driving factors when it comes to API's and there is no escaping where you fall.
- What language is it in
- How modern is it 
- What is the scale of the framework
- How is the community support

I can't really say that there is a wrong answer with any of them and if you can write one of them, you can pretty much write all of them. I did not feel like there was any framework I used that didn't have a feature I really wanted, that there wasn't a work around for. 
