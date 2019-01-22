
[![Wercker](https://img.shields.io/wercker/ci/wercker/docs.svg?maxAge=2592000)](https://github.com/speed/newcrawler-plugin-urlfetch-chrome)
[![npm](https://img.shields.io/npm/dm/localeval.svg?maxAge=2592000)](https://github.com/speed/newcrawler-plugin-urlfetch-chrome)
[![Coveralls branch](https://img.shields.io/coveralls/jekyll/jekyll/master.svg?maxAge=2592000)](https://github.com/speed/newcrawler-plugin-urlfetch-chrome)


# Install linux chrome

## Docker
```
  https://github.com/SeleniumHQ/selenium/wiki/Grid2
  https://github.com/SeleniumHQ/docker-selenium/tree/master/StandaloneChrome

  docker run -d -P -p 4444:4444 selenium/standalone-chrome
```

## Linux
```
  https://github.com/speed/selenium
  
  #Centos
  wget --no-check-certificate --no-verbose https://raw.githubusercontent.com/speed/selenium/master/Centos.sh -O Selenium-Chrome.sh \
  && sh Selenium-Chrome.sh
  
  #Ubuntu
  wget --no-check-certificate --no-verbose https://raw.githubusercontent.com/speed/selenium/master/Ubuntu.sh -O Selenium-Chrome.sh \
  && sh Selenium-Chrome.sh
```

# RemoteWebDriver chrome.driver

  http://127.0.0.1:4444/wd/hub



