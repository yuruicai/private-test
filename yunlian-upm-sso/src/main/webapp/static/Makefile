 
install:
	npm -r http://r.npm.fengjr.com install

build_login: entrance/login/* lib/*.js
	node tools/build.js --debug entrance/login/

build_user: entrance/user/* lib/*.js
	node tools/build.js --debug entrance/user/

build_pass: entrance/pass/* lib/*.js
	node tools/build.js --debug entrance/pass/

build: build_login build_user build_pass

clean:
	rm -f */*-v*.{css,js,ico} */*/*-v*.js
	git checkout ../WEB-INF

.PHONY: clean
