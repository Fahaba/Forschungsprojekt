import csv
import matplotlib.pyplot as plt
import utils
import defaults

file_jr = utils.get_file("jr", "tim")
file_sr = utils.get_file("sr", "tim")
file_gr = utils.get_file("gr", "tim")

file_jb = utils.get_file("jb", "tim")
file_sb = utils.get_file("sb", "tim")
file_gb = utils.get_file("gb", "tim")

# Backup if utils does not work properly
# def get_cycles_list(file):
#     print(file)
#     full_list = []
#     with open(file, newline='') as csv_file:
#         reader = csv.reader(csv_file, delimiter='\t')
#         for row in reader:
#             tmp_list = [row[0], row[1]]
#
#             start_time = utils.get_time_seconds(row[2])
#             end_time = utils.get_time_seconds(row[3])
#             duration = end_time - start_time
#
#             tmp_list.append(duration)
#             full_list.append(tmp_list)
#     return full_list


fig, (jb, sb, gb) = plt.subplots(3, 1)
sb_y_range = (0, 10)
gb_y_range = (0, 100)
jb_y_range = (0, 700)

y_range = gb_y_range

utils.plot_data(jb, file_jb)
jb.set(
    # title='Single JavaC',
    ylabel='javac (loop) Time in Sec.',
    # xlabel=('depth' if y_child else 'Nb. of Children'),
    ylim=jb_y_range,
    xticklabels=([])
)
jb.legend()

utils.plot_data(sb, file_sb)
sb.set(
    # title='Single JavaC',
    ylabel='javac (single) Time in Sec.',
    # xlabel=('depth' if y_child else 'Nb. of Children'),
    ylim=sb_y_range,
    xticklabels=([])
)

utils.plot_data(gb, file_gb)
gb.set(
    # title='Single JavaC',
    ylabel='gradle build Time in Sec.',
    xlabel=('depth' if defaults.y_axes_child else 'Nb. of Children'),
    ylim=gb_y_range
)

# plt.suptitle("Compilation Time Comparison")
# plt.show()

fig.subplots_adjust(
    left=0.06,
    right=0.98,
    top=0.98,
    bottom=0.06
)
fig.set_size_inches(12, 7)
fig.savefig(defaults.out_dir + "/time.svg", format="svg", ppi=300)