FROM python:2.7

RUN mkdir -p /wcoc2017pro
WORKDIR /wcoc2017pro/

ADD requirements.txt .
ADD templates .
ADD main.py .

RUN pip install -r requirements.txt

CMD python main.py
