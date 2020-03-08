import pyspark
from pyspark.sql import SparkSession
import pyspark.sql.functions
from operator import add
import math

# Create Spark Session and Context
sc = pyspark.SparkContext("spark://ec2-54-90-175-145.compute-1.amazonaws.com:7077", "test")
spark = SparkSession(sc)

# Load data
csv_data = spark.read.csv("hdfs://ec2-54-90-175-145.compute-1.amazonaws.com:9000/kindle_reviews.csv",inferSchema=True, header=True)

json_data = spark.read.json("hdfs://ec2-54-90-175-145.compute-1.amazonaws.com:9000/meta_kindle_exported.json")
join_data = csv_data.join(json_data, csv_data.asin == json_data.asin)

# Select the length of review text and price as rdd for map reduce
spark_df = join_data[pyspark.sql.functions.length('reviewText'), 'price']
sparkrdd =  spark_df.na.drop().rdd

# Flatmap of pearson correlation variables
map_output = sparkrdd.flatMap(lambda x : [(0,1), (1,x[0]), (2,x[1]), (3,x[0]**2), (4,x[1]**2), (5,x[0]*x[1])])

# Reduce by key
reduce_output = map_output.reduceByKey(lambda x, y: x + y)
collect_reduce_output = reduce_output.collect()

# Pearson correlation variables
n = collect_reduce_output[0][1]
sum_x = collect_reduce_output[1][1]
sum_y = collect_reduce_output[2][1]
sum_x_square = collect_reduce_output[3][1]
sum_y_square = collect_reduce_output[4][1]
sum_xy = collect_reduce_output[5][1]

# Calculate Pearson correlation
pearson_correlation = ((n*sum_xy) - (sum_x*sum_y)) / (math.sqrt((n*sum_x_square) - (sum_x**2)) * math.sqrt((n*sum_y_square) - (sum_y**2)))
print(pearson_correlation) 

with open('pearson_correlation_results.txt', 'w') as f:
    print(pearson_correlation, file=f)  # Python 3.x

sc.stop()
