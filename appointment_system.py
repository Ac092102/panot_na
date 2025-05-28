import tkinter as tk
from tkinter import messagebox
import mysql.connector
from datetime import datetime

conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",  
    database="school_appointments"
)
cursor = conn.cursor()


def submit_appointment():
    name = name_entry.get()
    grade = grade_entry.get()
    purpose = purpose_entry.get("1.0", tk.END).strip()
    date = date_entry.get()
    time = time_entry.get()

    if not (name and grade and purpose and date and time):
        messagebox.showerror("Input Error", "Please fill all fields.")
        return

    try:
        datetime.strptime(date, "%Y-%m-%d")
        datetime.strptime(time, "%H:%M")
    except ValueError:
        messagebox.showerror("Input Error", "Invalid date or time format.")
        return

    cursor.execute("INSERT INTO appointments (student_name, grade_level, purpose, appointment_date, appointment_time) VALUES (%s, %s, %s, %s, %s)",
                   (name, grade, purpose, date, time))
    conn.commit()
    messagebox.showinfo("Success", "Appointment scheduled successfully.")
    clear_fields()

def clear_fields():
    name_entry.delete(0, tk.END)
    grade_entry.delete(0, tk.END)
    purpose_entry.delete("1.0", tk.END)
    date_entry.delete(0, tk.END)
    time_entry.delete(0, tk.END)

root = tk.Tk()
root.title("School Appointment Scheduler")

tk.Label(root, text="Student Name").grid(row=0, column=0, padx=10, pady=5, sticky="e")
name_entry = tk.Entry(root, width=40)
name_entry.grid(row=0, column=1)

tk.Label(root, text="Grade Level").grid(row=1, column=0, padx=10, pady=5, sticky="e")
grade_entry = tk.Entry(root, width=40)
grade_entry.grid(row=1, column=1)

tk.Label(root, text="Purpose").grid(row=2, column=0, padx=10, pady=5, sticky="ne")
purpose_entry = tk.Text(root, height=4, width=30)
purpose_entry.grid(row=2, column=1, pady=5)

tk.Label(root, text="Date (YYYY-MM-DD)").grid(row=3, column=0, padx=10, pady=5, sticky="e")
date_entry = tk.Entry(root, width=40)
date_entry.grid(row=3, column=1)

tk.Label(root, text="Time (HH:MM)").grid(row=4, column=0, padx=10, pady=5, sticky="e")
time_entry = tk.Entry(root, width=40)
time_entry.grid(row=4, column=1)

submit_btn = tk.Button(root, text="Submit Appointment", command=submit_appointment)
submit_btn.grid(row=5, column=0, columnspan=2, pady=10)

root.mainloop()

