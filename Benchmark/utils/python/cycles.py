import matplotlib.pyplot as plt
import defaults
import utils

file_jr = utils.get_file("jr", "cyc")
file_sr = utils.get_file("sr", "cyc")
file_gr = utils.get_file("gr", "cyc")

file_jb = utils.get_file("jb", "cyc")
file_sb = utils.get_file("sb", "cyc")
file_gb = utils.get_file("gb", "cyc")

fig, (jb, sb, gb) = plt.subplots(3, 1)
sb_y_range = (0, 4e10)
gb_y_range = (0, 6e11)
jb_y_range = (0, 4e12)

y_range = gb_y_range

utils.plot_data(jb, file_jb)
jb.set(
    # title='Single JavaC',
    ylabel='javac (loop) Cycles',
    # xlabel=('depth' if y_child else 'Nb. of Children'),
    ylim=jb_y_range,
    xticklabels=([])
)
jb.legend()

utils.plot_data(sb, file_sb)
sb.set(
    # title='Single JavaC',
    ylabel='javac (single) Cycles',
    # xlabel=('depth' if y_child else 'Nb. of Children'),
    ylim=sb_y_range,
    xticklabels=([])
)

utils.plot_data(gb, file_gb)
gb.set(
    # title='Single JavaC',
    ylabel='gradle build Cycles',
    xlabel=('depth' if defaults.y_axes_child else 'Nb. of Children'),
    ylim=gb_y_range
)

# plt.suptitle("CPU-Cycles Comparison")
# plt.show()

fig.subplots_adjust(
    left=0.06,
    right=0.98,
    top=0.98,
    bottom=0.06
)

fig.set_size_inches(12, 7)
fig.savefig(defaults.out_dir + "/cycles.svg", format="svg", ppi=300)