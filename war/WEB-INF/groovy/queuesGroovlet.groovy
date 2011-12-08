assert defaultQueue

request.setAttribute 'queueName', defaultQueue.queueName

assert queues

def accessQueue = queues.default
request.setAttribute 'someQueue', accessQueue.queueName
def task = accessQueue.add()
request.setAttribute 'task', task.name
accessQueue.deleteTask(task)
