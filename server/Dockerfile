FROM wcoc2017pro/python2.7:latest

RUN mkdir -p /wcoc2017pro
WORKDIR /wcoc2017pro/

ADD templates ./templates
ADD main.py .

CMD python main.py --address="0.0.0.0"
