import defaults
import csv


def get_time_seconds(stamp):
    time_stamp = [int(s) for s in stamp.split(':')]
    time_nano = time_stamp[0] * 1000000000 + time_stamp[1]
    time_seconds = (time_nano / 1000000000)
    return time_seconds


def get_file(kind, resource):
    path = "{}/{}_c{}-{}_d{}-{}_{}.csv".format(
        defaults.src_dir,
        kind,
        defaults.cMin,
        defaults.cMax,
        defaults.dMin,
        defaults.dMax,
        resource
    )
    return path


def get_cycles_list(file):
    full_list = []
    with open(file, newline='') as csv_file:
        reader = csv.reader(csv_file, delimiter='\t')
        for row in reader:
            tmp_list = [row[0], row[1]]

            if 'cyc.csv' in file:
                y = row[2].replace(',', '')
                y = y.replace(':', '')
                time = int(y)

            elif 'tim.csv' in file:
                start_time = get_time_seconds(row[2])
                end_time = get_time_seconds(row[3])
                time = end_time - start_time
                
            else:
                print('Error wrong kind of file?')
                return

            tmp_list.append(time)
            full_list.append(tmp_list)
    return full_list


def split_list(l2split, chunks: int):
    l = len(l2split)
    chunk_size = int(l / chunks)
    new_list = []
    for i in range(0, chunks):
        st_pos = i * chunk_size
        chunk = l2split[st_pos:(st_pos + chunk_size)]
        print(chunk)
        new_list.append(chunk)
    return new_list


def plot_lists(sp, lists):
    for i in range(0, len(lists)):
        n_list = lists[i]
        y_list = []
        x_list = []

        for j in range(0, len(n_list)):
            y_list.append(n_list[j][2])
            x_list.append(n_list[j][1] if defaults.y_axes_child else n_list[j][0])

        lbl = str(i+1) + (" children" if defaults.y_axes_child else " depth")

        sp.plot(x_list, y_list, label=lbl)


def plot_data(sp, file):
    cyc_list = get_cycles_list(file)
    cyc_by_child = sorted(cyc_list, key=lambda x: (x[0], x[1]))
    cyc_by_depth = sorted(cyc_list, key=lambda x: (x[1], x[0]))
    complex_list = split_list(cyc_by_child, defaults.c_distance) \
        if defaults.y_axes_child \
        else split_list(cyc_by_depth, defaults.d_distance)
    plot_lists(sp, complex_list)
