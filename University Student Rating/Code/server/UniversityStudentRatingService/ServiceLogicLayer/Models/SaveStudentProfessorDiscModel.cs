using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Models
{
    public class SaveStudentProfessorDiscModel
    {
        public int StudentId { get; set; }
        public List<StudentProfessorDiscipline> StudentProfessorDisciplines { get; set; }
    }
}
