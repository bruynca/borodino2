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
                    """move_factor = ET.SubElement(brigade, 'moveFactor')
                    move_factor.text = '4'  # Set moveFactor to 4
                    combat_factor = ET.SubElement(brigade, 'combatfactor')
                    combat_factor.text = '4'  # Set moveFactor to 4
                    type_factor = ET.SubElement(brigade, 'type')
                    type_factor.text = 'infantry'  # Set moveFactor to 4
                    scen1x = ET.SubElement(brigade, 'scen1x')
                    scen1x.text = '22'  # Set moveFactor to 4
                    scen1y = ET.SubElement(brigade, 'scen1y')
                    scen1y.text = '22'  # Set moveFactor to 4
                    scen2x = ET.SubElement(brigade, 'scen2x')
                    scen2x.text = '22'  # Set moveFactor to 4
                    scen2y = ET.SubElement(brigade, 'scen2y')
                    scen2y.text = '22'  # Set moveFactor to 4
                    scen3x = ET.SubElement(brigade, 'scen3x')
                    scen3x.text = '22'  # Set moveFactor to 4
                    scen3y = ET.SubElement(brigade, 'scen3y')
                    scen3y.text = '22'  # Set moveFactor to 4
                    scen4x = ET.SubElement(brigade, 'scen4x')
                    scen4x.text = '22'  # Set moveFactor to 4
                    scen4y = ET.SubElement(brigade, 'scen4y')
                    scen4y.text = '22'  # Set moveFactor to 4
                    origin = ET.SubElement(brigade, 'origin')
                    origin.text = 'french'  # Set moveFactor to 4
                    type = ET.SubElement(brigade, 'type')
                    type.text = 'infantry'  # Set moveFactor to 4
                    entry1 = ET.SubElement(brigade, 'entry1')
                    entry1.text = '01'  # Set moveFactor to 4
                    entry2 = ET.SubElement(brigade, 'entry2')
                    entry2.text = '01'  # Set moveFactor to 4
                    entry3 = ET.SubElement(brigade, 'entry3')
                    entry3.text = '01'  # Set moveFactor to 4
                    entry4 = ET.SubElement(brigade, 'entry4')
                    entry4.text = '01'  # Set moveFactor to 4"""
        # Check for Brigade on a separate line
        elif 'Brigade' in line and current_division is not None:
            brigade_name = re.search(r'\S+', line.split('Brigade', 1)[1].strip()).group()
            brigade = ET.SubElement(current_division, 'Brigade')
            brigade.set('name', brigade_name)
          """  move_factor = ET.SubElement(brigade, 'moveFactor')
            move_factor.text = '4'  # Set moveFactor to 4"""

# Create an ElementTree object
tree = ET.ElementTree(root)

# Convert the XML to a string with pretty formatting
xmlstr = minidom.parseString(ET.tostring(root)).toprettyxml(indent="  ")

# Write the pretty formatted XML to the file
with open(output_file, "w", encoding='utf-8') as f:
    f.write(xmlstr)