FROM dynamicip-chrome-java-base

ADD --chown=seluser:seluser src/main/resources /opt/dynamicip/scraping-example
ADD --chown=seluser:seluser target/com.dynamicip.example-0.1.0-SNAPSHOT-jar-with-dependencies.jar /opt/dynamicip/scraping-example/com.dynamicip.example.jar
ADD --chown=seluser:seluser .dynamicip_api_key /opt/dynamicip/scraping-example
RUN cd /opt/dynamicip/scraping-example && \
    sed -i -e "s/___APIKEY___/$(cat .dynamicip_api_key)/g" chrome_extension/authenticator.js && \
    sudo cp entrypoint.sh /opt/bin/entry_point.sh