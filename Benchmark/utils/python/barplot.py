import csv
import argparse
import matplotlib.pyplot as plt
import numpy as np

file_gr = "D:/Temp/data/gr_c1-4_d1-5_cyc.csv"
file_jr = "D:/Temp/data/jr_c1-4_d1-5_cyc.csv"
file_gb = "D:/Temp/data/gb_c1-4_d1-5_cyc.csv"
file_jb = "D:/Temp/data/jb_c1-4_d1-5_cyc.csv"


def get_cycles(file):
    x_list = []
    y_list = []
    with open(file, newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter='\t')
        for row in reader:
            x_list.append(str(row[0]) + str(row[1]))
            y = row[2].replace(',','')
            y_list.append(int(y))
    return (x_list, y_list)


jb_x = []
jb_y = []
gb_x = []
gb_y = []
jr_x = []
jr_y = []
gr_x = []
gr_y = []

(jb_x, jb_y) = get_cycles(file_jb)
(gb_x, gb_y) = get_cycles(file_gb)
(jr_x, jr_y) = get_cycles(file_jr)
(gr_x, gr_y) = get_cycles(file_gr)

x = np.arange(len(gb_x))  # the label locations
width = 0.2  # the width of the bars

fig, ax = plt.subplots()
rects1 = ax.bar(x - width/2 - width, gb_y, width, label='gradle build')
rects2 = ax.bar(x - width/2, jb_y, width, label='javac')
rects3 = ax.bar(x + width/2, gr_y, width, label='gradle run')
rects4 = ax.bar(x + width/2 + width, jr_y, width, label='java')

# rects1 = ax.bar(x - 1.5 * width, gb_y, width, label='gradle build')
# rects2 = ax.bar(x - 0.5 * width, jb_y, width, label='javac')
# rects3 = ax.bar(x + 0.5 * width, gr_y, width, label='gradle run')
# rects4 = ax.bar(x + 1.5 * width, jr_y, width, label='java')

# Add some text for labels, title and custom x-axis tick labels, etc.
ax.set_ylabel('CPU Cycles')
ax.set_title('CPU Cycles: Java vs. Gradle')
ax.set_xticks(x)
ax.set_xticklabels(gb_x)
ax.legend()

fig.tight_layout()
plt.show()
