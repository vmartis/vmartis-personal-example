#!/bin/bash

xxd -p invoice-template-eur.docx | tr -d '\n' > invoice-template-eur.hex
xxd -p ./logo.png | tr -d '\n' > logo.hex
xxd -p ./stamp.jpg | tr -d '\n' > stamp.hex