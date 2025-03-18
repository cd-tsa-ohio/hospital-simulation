#importing relevant modules
import json
import requests
#import CL_reader
from plotly.graph_objs import Bar, Layout
from plotly import offline

#defining filename
filename = 'nyt_counties.json'
with open(filename) as f:
	nyt_counties = json.load(f)

#gathering data
casesdict = []
datedict = []

for nyt in nyt_counties:
	case = nyt['cases']
	if nyt['county'] == 'Athens':
		casesdict.append(case)

for nyt in nyt_counties:
	date = nyt['date']
	if nyt['county'] == 'Athens':
		datedict.append(date)
		
#plotting barchart
y_axis_config = {'title': 'Number of Cases'}
x_axis_config = {'title': 'Date'}
x_values = datedict
y_values = casesdict

data = [Bar (x=x_values, y=y_values)]
my_layout = Layout(title='Athens County Coronavirus Cases per day', xaxis=x_axis_config, yaxis=y_axis_config)
offline.plot({'data':data, 'layout':my_layout}, filename='Athenscasesperday.html')
