import subprocess
import sys

def convert_pdf_to_html(pdf_path, output_name, output_path):
    try:
        command = "pdf2htmlEX --dest-dir " + output_path + " " + pdf_path + " " +  output_name + ""
        response = subprocess.check_output(command, shell=True, stderr=subprocess.STDOUT)
        print("PDF converted to HTML successfully!")
    except subprocess.CalledProcessError as e:
        print("Error converting PDF to HTML:")
if len(sys.argv) != 4:
        print("Usage: python3 pdf_to_html.py input_pdf output_html")
else:
        arg1 = sys.argv[1]
        arg2 = sys.argv[2]
        arg3 = sys.argv[3]
        convert_pdf_to_html(arg1, arg2, arg3)
