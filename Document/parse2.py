import xml.etree.ElementTree as ET
import re
from xml.dom import minidom

# Input and output file names
input_file = 'french.txt'
output_file = 'output.xml'

# Create the root element
root = ET.Element('FrenchArmy')

current_corp = None
current_division = None

# Read the input file and create XML structure
with open(input_file, 'r') as file:
    for line in file:
        # Strip any leading/trailing whitespace
        line = line.strip()
        
        # Check for Corps
        if 'Corps' in line:
            corp_name = re.search(r'\S+', line).group()
            current_corp = ET.SubElement(root, 'Corp')
            current_corp.set('name', corp_name)
            current_division = None  # Reset when moving to a new Corps
        
        # Check for Division
        if 'Division' in line and current_corp is not None:
            division_name = re.search(r'\S+', line).group()
            current_division = ET.SubElement(current_corp, 'Division')
            current_division.set('name', division_name)
        
            # Check for Brigade on the same line
            if 'Brigade' in line:
                brigade_name = re.search(r'Brigade\s+(\S+)', line)
                if brigade_name:
                    brigade_name = brigade_name.group(1)
                    brigade = ET.SubElement(current_division, 'Brigade')
                    brigade.set('name', brigade_name)
        # Check for Brigade on a separate line
        elif 'Brigade' in line and current_division is not None:
            brigade_name = re.search(r'\S+', line.split('Brigade', 1)[1].strip()).group()
            brigade = ET.SubElement(current_division, 'Brigade')
            brigade.set('name', brigade_name)

# Create an ElementTree object
tree = ET.ElementTree(root)

# Convert the XML to a string with pretty formatting
xmlstr = minidom.parseString(ET.tostring(root)).toprettyxml(indent="  ")

# Write the pretty formatted XML to the file
with open(output_file, "w", encoding='utf-8') as f:
    f.write(xmlstr)