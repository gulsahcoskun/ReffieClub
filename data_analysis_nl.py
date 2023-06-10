import pandas as pd
import numpy as np
import plotly.express as px
from collections import Counter

data_nl = pd.read_csv('sourcestack-data-NL.csv')

### --- Analysis of the data for the Netherlands ---###

# Cleaning the NL data
# Select only records with country = 'Netherlands'
print('Initial length of the dataset', len(data_nl))
data_nl = data_nl[data_nl['country'] == 'Netherlands']
print('The length of data after removing records that have jobs outside of the Netherlands',
      len(data_nl))  # 500 initial records - 486 records after filtering = 14 records removed

# Harmonise the name of the cities
data_nl['city'] = data_nl['city'].replace(np.nan, 'Not Specified')
data_nl['city'] = data_nl['city'].replace('Delft Area', 'Delft')
data_nl['city'] = data_nl['city'].replace('Europoort Rotterdam', 'Rotterdam')
data_nl['city'] = data_nl['city'].replace('Netherlands - Amsterdam', 'Amsterdam')
data_nl['city'] = data_nl['city'].replace('NL - Amsterdam', 'Amsterdam')
data_nl['city'] = data_nl['city'].replace('Schiphol-Rijk', 'Schiphol')
data_nl['city'] = data_nl['city'].replace('Schiphol Airport', 'Schiphol')
data_nl['city'] = data_nl['city'].replace('Den Haag', 'The Hague')
data_nl['city'] = data_nl['city'].replace('Den Helder', 'The Helder')
data_nl['city'] = data_nl['city'].replace('Den Bosch', 'The Bosch')

# Drop Not Specified, Multiple Cities, Nederland, South Holland, Holland
data_nl = data_nl[data_nl['city'] != 'Not Specified']
data_nl = data_nl[data_nl['city'] != 'Multiple Cities']
data_nl = data_nl[data_nl['city'] != 'Nederland']
data_nl = data_nl[data_nl['city'] != 'South Holland']
data_nl = data_nl[data_nl['city'] != 'Holland']

# Replace other data labels
data_nl['remote'] = data_nl['remote'].replace(True, 'Yes')
data_nl['remote'] = data_nl['remote'].replace(False, 'No')
data_nl['hours'] = data_nl['hours'].replace(np.nan, 'Not Specified')
data_nl['hours'] = data_nl['hours'].replace('Temp', 'Temporary')

print('Length after cleaning cities:', len(data_nl))

# Utility function 1
def plot_job_offers(data):
    fig = px.bar(
        data,
        orientation='h',
        text_auto='',
        color_discrete_sequence=['#3b4856'],  # Set bar color
        template='plotly_white'
    )

    fig.update_layout(
        title_text='Top cities with the most job offers in the Netherlands',
        title_font_size=20,
        showlegend=False,
        title_x=0.5,
        font=dict(size=16)
    )

    fig.update_layout(width=2000, height=1200)  # Set figure size
    fig.update_layout(dragmode=False)  # Disable dragging the plot

    fig.update_traces(
        textposition='auto',
        textfont_color='#232323',  # Set text color
        marker_line_color='#232323'  # Set marker border color
    )

    fig.update_traces(textfont_size=18, textangle=0, textposition="outside", cliponaxis=False)  # Set text size and position

    fig.update_xaxes(title_text='Number of jobs')
    fig.update_yaxes(title_text='')

    #fig.show()

filtered_jobs = data_nl['city'].value_counts().loc[lambda x: x >= 2].sort_values(ascending=True)
plot_job_offers(filtered_jobs)


# Utility function 2
def filter_job_types(data):
    data_filtered = data[(data['hours'] != 'Not Specified') & (data['hours'] != 'Contract') & (data['hours'] != 'Unclear')]
    return data_filtered

data_nl_job_types = filter_job_types(data_nl)
num_jobs_types = len(data_nl_job_types)
data_nl_remote = data_nl_job_types['remote'].dropna()
num_jobs_remote = len(data_nl_remote)
data_nl_seniority = data_nl_job_types.dropna(subset=['seniority'])
num_jobs_seniority = len(data_nl_seniority)


# Pie plot of the job types
fig = px.pie(data_nl_job_types, names='hours', hole=.4,
             color_discrete_sequence=px.colors.sequential.RdBu)
fig.update_layout(
    annotations=[
        dict(
            text='Job Types',
            x=0.5,
            y=0.55,
            font_size=32,
            showarrow=False,
            align='center',
        ),
        dict(
            text=f'*{num_jobs_types} jobs',
            x=0.5,
            y=0.45,
            font_size=18,
            showarrow=False,
            align='center',
        )
    ],
    legend=dict(
        orientation="h",
        y=1.1,
        x=0.5,
        xanchor='center',
        font=dict(size=16)
    )
)
fig.update_traces(textfont=dict(size=14))
#fig.show()


# Pie plot of if the job is remote or not
fig = px.pie(data_nl_remote, names='remote', hole=.4,
             color_discrete_sequence=px.colors.sequential.RdBu)
fig.update_layout(
    annotations=[
        dict(
            text='Remote Jobs',
            x=0.5,
            y=0.55,
            font_size=32,
            showarrow=False,
            align='center',
        ),
        dict(
            text=f'*{num_jobs_remote} jobs',
            x=0.5,
            y=0.45,
            font_size=18,
            showarrow=False,
            align='center',
        )
    ],
    legend=dict(
        orientation="h",
        y=1.1,
        x=0.5,
        xanchor='center',
        font=dict(size=16)
    )
)
#fig.show()

# Pie plot of the seniority level of the job
fig = px.pie(data_nl_seniority, names='seniority', hole=.4, color_discrete_sequence=px.colors.sequential.RdBu)
fig.update_layout(
    annotations=[
        dict(
            text='Seniority',
            x=0.5,
            y=0.55,
            font_size=32,
            showarrow=False,
            align='center',
        ),
        dict(
            text=f'*{num_jobs_seniority} jobs',
            x=0.5,
            y=0.45,
            font_size=18,
            showarrow=False,
            align='center',
        )
    ],
    legend=dict(
        orientation="h",
        y=1.1,
        x=0.5,
        xanchor='center',
        font=dict(size=16)
    )
)
fig.update_traces(textfont=dict(size=14))
#fig.show()

# Pie plot for skills
print('Skills:', data_nl['tags_matched'].value_counts())
# Extract all skills from the column 'tags_matched' and put them in a list
print(data_nl['tags_matched'].dtype)
# Number of job posts analysed
num_jobs_skills = len(data_nl['tags_matched'])

# Extract all skills from the column 'tags_matched' and put them in a list
skills = []
for i in data_nl['tags_matched']:
    if type(i) == str:
        skills.append(i.split(','))
    else:
        skills.append(i)

# Flatten the list
skills = [item for sublist in skills for item in sublist]

# How to extract the skills from [] to a single list
skills = [item.strip('[]') for item in skills]
skills_frequency = Counter(skills)

# Convert the Counter object to a DataFrame
skills_frequency = pd.DataFrame.from_dict(skills_frequency, orient='index', columns=['Frequency'])

# Reshape the DataFrame
skills_frequency = skills_frequency.reset_index().rename(columns={'index': 'Skill'}).rename_axis(None, axis=1).sort_values('Frequency', ascending=False)

# Remove empty skills
skills_frequency = skills_frequency[skills_frequency['Skill'] != '']

# Reset the index
skills_frequency = skills_frequency.reset_index(drop=True)



fig = px.pie(skills_frequency.head(20), names='Skill', values='Frequency', hole=.4,
             color_discrete_sequence=px.colors.sequential.RdBu)
fig.update_layout(
    annotations=[
        dict(
            text='Top 20 Skills',
            x=0.5,
            y=0.55,
            font_size=28,
            showarrow=False,
            align='center',
        ),
        dict(
            text=f'Based on {num_jobs_skills} job offers',
            x=0.5,
            y=0.45,
            font_size=14,
            showarrow=False,
            align='center',
        )
    ],
    legend=dict(
        orientation="h",
        y=1.1,
        x=0.5,
        xanchor='center',
        font=dict(size=16)
    ),
    margin=dict(l=20, r=20, t=20, b=20)  # Adjust the margin values here
)
fig.update_traces(textfont=dict(size=14))
#fig.show()







