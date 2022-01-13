import csv
import matplotlib.pyplot as plt
import utils
import defaults

file_jr = utils.get_file("jr", "mem")
file_sr = utils.get_file("sr", "mem")
file_gr = utils.get_file("gr", "mem")

file_jb = utils.get_file("jb", "mem")
file_sb = utils.get_file("sb", "mem")
file_gb = utils.get_file("gb", "mem")


def create_image(sp, mem_file):
    # create memory graph
    time = []
    memo = []
    axes = []
    zero_time = 0

    with open(mem_file, newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter='\t')
        for row in reader:
            if zero_time == 0:
                zero_time = utils.get_time_seconds(row[0])
            time_sec = utils.get_time_seconds(row[0]) - zero_time
            time.append(time_sec)
            memo.append(int(row[1]) / 1000)  # KB to MB

            if len(row) > 2:
                axes.append(time_sec)

    sp.axvline(axes.pop(), color='orange', linestyle='--', label="Run Seperator")
    for axe in axes:
        sp.axvline(axe, color='orange', linestyle='--')
    sp.plot(time, memo, 'g', label='Memory')


fig, (jb, sb, gb) = plt.subplots(3, 1)
jb_y_range = (1070, 1300)
sb_y_range = (1000, 1700)
gb_y_range = (1000, 5000)

y_range = (1500, 2300)

jb.set(
    # title='Single JavaC',
    ylabel='javac (loop) RAM (MB)',
    # xlabel='Time/Seconds',
    ylim=jb_y_range
)

create_image(jb, file_jb)
jb.legend(loc = 1)


sb.set(
    # title='Single JavaC',
    ylabel='javac (single) RAM (MB)',
    # xlabel='Time/Seconds',
    ylim=sb_y_range
)
create_image(sb, file_sb)
gb.set(
    # title='Single JavaC',
    ylabel='gradle build RAM (MB)',
    xlabel='Time/Seconds',
    ylim=gb_y_range
)
create_image(gb, file_gb)


# plt.suptitle("Memory Usage")
# plt.legend()
plt.show()

fig.subplots_adjust(
    left=0.06,
    right=0.98,
    top=0.98,
    bottom=0.06
)

fig.set_size_inches(12, 7)
fig.savefig(defaults.out_dir + "/memory.svg", format="svg", ppi=300)


# create proto file
file = "D:/Git/ForschungsProjekt/Benchmark/out/data/c1-5_d1-5_mem.csv"
fig, sp = plt.subplots(1, 1)
create_image(sp, file)
sp.set(
    # title='Single JavaC',
    ylabel='RAM Usage(MB)',
    xlabel='Time/Seconds',
)
fig.subplots_adjust(
    left=0.06,
    right=0.98,
    top=0.98,
    bottom=0.06
)
fig.legend()
fig.set_size_inches(15, 5)
fig.savefig(defaults.out_dir + "/proto.svg", format="svg", ppi=300)
