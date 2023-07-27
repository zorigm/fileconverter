import os
from  hwp5 import hwp5html
from bs4 import BeautifulSoup
import sys

def hwp_to_html(input_file):
    # Ensure the input file exists
    if not os.path.isfile(input_file):
        print("Error: The file does not exist.")
        return

    # Create a new HTML file
    html_file = os.path.splitext(input_file)[0] + ".xhtml"

    with open(input_file, "rb") as f:

        # Extract the HWP content to a BeautifulSoup object
        soup = BeautifulSoup(f, 'html.parser')
    [s.extract() for x in soup.find_all('script')]
    [s.extract() for x in soup.find_all('style')]
    [s.extract() for x in soup.find_all('meta')]
    [s.extract() for x in soup.find_all('noscript')]
    html_content = soup.prettify("utf-8")
    
    with open(html_file, "wb") as file:
        file.write(html_content)

    print(f"Conversion completed. HTML file saved as {html_file}")

if len(sys.argv) != 2:
        print("Usage: python3 hwp_to_html2.py input file")
else:
        arg1 = sys.argv[1]
        
        hwp_to_html(arg1)