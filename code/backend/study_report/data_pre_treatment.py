# coding=gbk
import numpy as np
import pandas as pd
import pymysql
import featuretools as ft

# python�������ݿ�test50�������α�
db=pymysql.connect(host='localhost', port=3306, user='root', passwd='abcd.1234',
                   db='onlineStudy', charset='utf8')
cur = db.cursor()
pd.set_option('max_colwidth', 200)
# ��ȡ���ݿ��б�������˳��ά�ֲ���


def sql2df(name_cur, name_tab_str):
    # ��ȡ��������
    sql_data = '''select * from %s; ''' % name_tab_str
    name_cur.execute(sql_data)
    data=name_cur.fetchall()
    # ��ȡ����
    cols = [i[0] for i in name_cur.description]
    # sql�ڱ�ת��pandas��DF
    df = pd.DataFrame(np.array(data), columns=cols)
    return df


df_student = sql2df(cur,'user')
df_study_record = sql2df(cur, 'study_record')

for index, row in df_study_record.iterrows():
    row['time_in_minute'] = row['time_in_minute'] - row['pause_time']

df_study_record['user_id'] = df_study_record['user_id'].astype(np.int64)
df_student['id'] = df_student['id'].astype(np.int64)

df_student = pd.DataFrame(df_student['id'])

df_study_record.drop('change_speed_time', axis=1)
df_study_record.drop('jump_times', axis=1)
df_study_record.drop('pause_time', axis=1)

es = ft.EntitySet(id='students')

es = es.entity_from_dataframe(entity_id='student',
                              dataframe=df_student,
                              index='id',
                              )


es = es.entity_from_dataframe(entity_id='study_time',
                              dataframe=df_study_record,
                              make_index=True,
                              index='study_time_id',
                              variable_types={'study_date': ft.variable_types.Datetime,
                                              'time_in_minute': ft.variable_types.Numeric
                                              }
                              )

r_student_study_time = ft.Relationship(es['student']['id'],
                                       es['study_time']['user_id']
                                       )

es.add_relationship(r_student_study_time)

features, feature_names = ft.dfs(entityset=es,
                                 target_entity='student',
                                 agg_primitives=['mean', 'Std']
                                 )
file = open('1.csv', mode='w+')
features.to_csv('1.csv')
file.close()
