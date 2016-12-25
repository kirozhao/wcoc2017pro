#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

import os

import tornado.options
import tornado.web
from tornado.ioloop import IOLoop
from tornado.tcpserver import TCPServer

from tornado.options import options, define

import struct

define("address", default='127.0.0.1', type=str)
tornado.options.parse_command_line()


path = lambda root, *a: os.path.join(root, *a)
ROOT = os.path.dirname(os.path.abspath(__file__))

STATIC_ROOT = path(ROOT, 'static')
TEMPLATE_ROOT = path(ROOT, 'templates')

settings = {
    'static_path': STATIC_ROOT,
    'template_loader': tornado.template.Loader(TEMPLATE_ROOT)
}


class HelloHandler(tornado.web.RequestHandler):
    def data_received(self, chunk):
        pass

    def post(self):
        data = tornado.escape.json_decode(self.request.body)
        print data
        self.write(tornado.escape.json_encode({
            "banner": "%s counting" % data["user"]
        }))


class ScanHandler(tornado.web.RequestHandler):
    visit_count = 0

    def data_received(self, chunk):
        pass

    def get(self):
        ScanHandler.visit_count += 1
        ProxyConnection.broadcast_visit_count(ScanHandler.visit_count)
        self.render('scan_result.html', visit_count=ScanHandler.visit_count)


url_patterns = [
    (r"/hello", HelloHandler),
    (r"/scan", ScanHandler),
]


class ProxyConnection(object):
    clients = set()

    def __init__(self, stream, address):
        ProxyConnection.clients.add(self)
        self._stream = stream
        self._address = address
        self._stream.set_close_callback(self.on_close)
        self.read_message()
        print "client connected", address

    def read_message(self):
        self._stream.read_bytes(20, self.process_message)

    def process_message(self, data):
        header_len, magic, product_id, cmdId, seq, body_len = struct.unpack('!ihhiii', data)
        print 'processing client req', header_len, magic, product_id, cmdId, seq, body_len

        if cmdId == 6:
            self._stream.write(struct.pack('!ihhiii', header_len, magic, product_id, cmdId, seq, body_len))

        self.read_message()

    @staticmethod
    def broadcast_visit_count(visit_count):
        print "broadcasting visit count: %d" % visit_count
        for conn in ProxyConnection.clients:
            conn.send_message(visit_count)

    def send_message(self, data):
        print "sending message", data, "to client ", self._address

        try:
            data = bytes(data)
            buf = struct.pack("!ihhiii%ds" % len(data), 20, 0, 0, 100, 0, len(data), data)
            print repr(buf)
            self._stream.write(buf)

        except Exception, e:
            print Exception, ":", e

    def on_close(self):
        print "client disconnected", self._address
        ProxyConnection.clients.remove(self)


class Application(tornado.web.Application):
    def __init__(self):
        tornado.web.Application.__init__(self, url_patterns, **settings)


class ProxyServer(TCPServer):
    def handle_stream(self, stream, address):
        ProxyConnection(stream, address)


if __name__ == "__main__":
    app = Application()
    app.listen(port=8000, address=options.address)

    proxy = ProxyServer()
    proxy.listen(port=8001, address=options.address)

    print "start demo server"
    IOLoop.current().start()
