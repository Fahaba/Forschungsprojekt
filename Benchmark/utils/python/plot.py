import csv
import argparse
import matplotlib.pyplot as plt

# default values
child_min = 1
child_max = 2
depth_min = 1
depth_max = 2
csv_dir = "../results/"

parser = argparse.ArgumentParser(description='visualize our benchmark results')
parser.add_argument("--out_dir", type=str, help="directory of csv files")
parser.add_argument("--children", type=str, help="number of children either [max] or [min:max]")
parser.add_argument("--depth", type=str, help="depth of the tree either [max] or [min:max]")
args = parser.parse_args()

if args.out_dir:
    csv_dir = args.out_dir
if args.children:
    children = args.children.split(':')
    if len(children) > 1:
        child_min = int(children[0])
        child_max = int(children[1])
    else:
        child_max = int(children[0])
if args.depth:
    depth = args.depth.split(':')
    if len(depth) > 1:
        depth_min = int(depth[0])
        depth_max = int(depth[1])
    else:
        depth_max = int(depth[0])

# files to read
mem_file_template = "{}/+c{}-{}_d{}-{}_mem.csv".format(csv_dir, child_min, child_max, depth_min, depth_max)
tim_file_template = "{}/+c{}-{}_d{}-{}_tim.csv".format(csv_dir, child_min, child_max, depth_min, depth_max)
mem_file_template=mem_file_template.replace("+", "{}")
tim_file_template=tim_file_template.replace("+", "{}")


def get_time_seconds(stamp):
    time_stamp = [int(s) for s in stamp.split(':')]
    time_nano = time_stamp[0] * 1000000000 + time_stamp[1]
    time_seconds = (time_nano / 1000000000)
    return time_seconds


def create_images(mem_file, tim_file):
    # create memory graph
    time = []
    memo = []
    axes = []
    zero_time = 0

    with open(mem_file, newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter='\t')
        for row in reader:
            if zero_time == 0:
                zero_time = get_time_seconds(row[0])
            time_sec = get_time_seconds(row[0]) - zero_time
            time.append(time_sec)
            memo.append(int(row[1]) / 1000)  # KB to MB

            if len(row) > 2:
                axes.append(time_sec)

    # plt.subplot(211)
    plt.subplot(111)
    plt.plot(time, memo, 'g', label='Memory')
    plt.xlabel('Time (in Seconds)')
    plt.ylabel('Memory (in MegaBytes)')
    plt.title('Memory Consumption')

    plt.axvline(axes.pop(), color='orange', linestyle='--', label="Run Seperator")
    for axe in axes:
        plt.axvline(axe, color='orange', linestyle='--')
    plt.legend()

    fig = plt.gcf()
    fig.set_size_inches(40, 15)
    fig.savefig(mem_file + ".svg", format="svg", ppi=300)

    # create time graphs
    child = []
    depth = []
    color = []
    duration = []

    with open(tim_file, newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter='\t')
        for row in reader:
            start_time = get_time_seconds(row[2])
            end_time = get_time_seconds(row[3])
            duration.append((end_time - start_time))

            nb_child = int(row[0])
            nb_depth = int(row[1])

            child.append(nb_child)
            depth.append(nb_depth)

            col = ((nb_child - (depth_min -1)) / (child_max - (child_min - 1)),
                   (nb_depth - (depth_min -1)) / (depth_max - (depth_min - 1)),
                0)
            color.append(col)

    # create new figure for the next 2 graphs
    plt.figure()
    # plt.subplot(223)
    plt.subplot(121)
    plt.scatter(child, duration, c=color)
    plt.xlabel("Nb. of Children")
    plt.ylabel("Compile Time")
    plt.title('Time/Children Dependency')
    plt.grid(True)

    # plt.subplot(224)
    plt.subplot(122)
    plt.scatter(depth, duration, c=color)
    plt.xlabel("Depth")
    plt.ylabel("Compile Time")
    plt.title("Time /Depth Dependency")
    plt.grid(True)

    fig = plt.gcf()
    fig.set_size_inches(18.5, 10.5)
    fig.savefig(tim_file + ".svg", format="svg", ppi=300)


create_images(
    mem_file_template.format(""), 
    tim_file_template.format(""))

create_images(
    mem_file_template.format("jb_"), 
    tim_file_template.format("jb_"))

create_images(
    mem_file_template.format("jr_"), 
    tim_file_template.format("jr_"))

create_images(
    mem_file_template.format("gb_"), 
    tim_file_template.format("gb_"))

create_images(
    mem_file_template.format("gr_"), 
    tim_file_template.format("gr_"))

create_images(
    mem_file_template.format("sr_"), 
    tim_file_template.format("sr_"))
    
create_images(
    mem_file_template.format("sb_"), 
    tim_file_template.format("sb_"))